/*
**
**  Mar. 1, 2008
**
**  The author disclaims copyright to this source code.
**  In place of a legal notice, here is a blessing:
**
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
**
**                                         Stolen from SQLite :-)
**  Any feedback is welcome.
**  Kohei TAKETA <k-tak@void.in>
**
*/
#ifndef ___CMECAB_INTERNAL___
#define ___CMECAB_INTERNAL___

// Standard Libraries
#include <string>
#include <algorithm>
#include <vector>
#include <cassert>
#include <exception>
#include <stdexcept>
#include <iostream>

// Java Native Interface
#include <jni.h>

// MeCab
#include <mecab.h>

#define DECLARE_CLASS(var, env, cls, ret) \
    jclass var;\
    var = env->FindClass(cls);\
    if (!var) return ret;

#define DECLARE_CLASS_NORET(var, env, cls) \
    jclass var;\
    var = env->FindClass(cls);\
    if (!var) return;

#define DECLARE_METHOD(var, env, cls, name, sgn, ret) \
    jmethodID var;\
    var = env->GetMethodID(cls, name, sgn);\
    if (!var) return ret;

#define DECLARE_FIELD(var, env, cls, name, sgn, ret) \
    jfieldID var;\
    var = env->GetFieldID(cls, name, sgn);\
    if (!var) return ret;

#define DECLARE_FIELD_NORET(var, env, cls, name, sgn) \
    jfieldID var;\
    var = env->GetFieldID(cls, name, sgn);\
    if (!var) return;

#define CLASS_MECAB_EXCEPTION \
    "net/moraleboost/mecab/MeCabException"
#define CLASS_OUT_OF_MEMORY_ERROR "java/lang/OutOfMemoryError"


/**
 * Java配列の内容取得に失敗した場合にスローされる
 */
class ArrayException : public std::exception
{
public:
    ArrayException()
        : std::exception()
    {}

    virtual ~ArrayException() throw()
    {}

    virtual const char* what() const throw()
    {
        return "Can't get array elements.";
    }
};

class ByteArrayHelper
{
protected:
    JNIEnv*     env_;
    jbyteArray  array_;
    jboolean    isCopy_;
    jbyte*      ptr_;
    jint        length_;
    bool        readOnly_;

public:
    ByteArrayHelper(JNIEnv* env, jbyteArray array, bool readOnly=false)
    {
        env_ = env;
        array_ = array;
        isCopy_ = false;
        ptr_ = 0;
        length_ = 0;
        readOnly_ = readOnly;

        getArray();
    }

    ByteArrayHelper(JNIEnv* env, jsize len)
    {
        env_ = env;
        array_ = 0;
        isCopy_ = false;
        ptr_ = 0;
        length_ = 0;
        readOnly_ = false;
        
        allocateArray(len);
    }

    virtual ~ByteArrayHelper()
    {
        releaseArray();
    }

    jbyteArray array() const
    {
        return array_;
    }

    jbyte* ptr() const
    {
        return ptr_;
    }

    jint length() const
    {
        return length_;
    }

    bool isCopy() const
    {
        return isCopy_;
    }

protected:
    void getArray()
    {
        length_ = env_->GetArrayLength(array_);
        ptr_ = (jbyte*)env_->GetPrimitiveArrayCritical(array_, &isCopy_);
        // ptr_ = env_->GetByteArrayElements(array_, &isCopy_);
        if (!ptr_) {
            throw ArrayException();
        }
    }

    void allocateArray(jsize len)
    {
        array_ = env_->NewByteArray(len);
        if (!array_) {
            throw std::bad_alloc();
        } else {
            getArray();
        }
    }

    void releaseArray()
    {
        if (ptr_) {
            // env_->ReleaseByteArrayElements(array_, ptr_,
            //                                readOnly_ ? JNI_ABORT : 0);
            env_->ReleasePrimitiveArrayCritical(array_, ptr_,
                                                readOnly_ ? JNI_ABORT : 0);
        }
    }
};


class TaggerHelper
{
protected:
    MeCab::Tagger*      tagger_;
    std::vector<char>   buf_;

public:
    TaggerHelper(const char* arg)
    {
        tagger_ = MeCab::createTagger(arg);
        if (!tagger_) {
            throw std::bad_alloc();
        }
    }

    virtual ~TaggerHelper()
    {
        delete tagger_;
    }

    MeCab::Tagger* tagger() const
    {
        return tagger_;
    }

    const MeCab::Node* parse(const char* str, size_t len)
    {
        buf_.resize(len+1);
        std::copy(str, str+len, &buf_[0]);
        buf_[len] = 0;
        return tagger_->parseToNode(&buf_[0], len);
    }
};

inline TaggerHelper* hdl2tagger(const jlong hdl)
{
    assert(hdl != 0);
    assert(sizeof(jlong) >= sizeof(TaggerHelper*));
    return reinterpret_cast<TaggerHelper*>(hdl);
}

inline jlong tagger2hdl(TaggerHelper* p)
{
    assert(p != NULL);
    assert(sizeof(jlong) >= sizeof(TaggerHelper*));
    return reinterpret_cast<jlong>(p);
}

inline const MeCab::Node* hdl2node(const jlong hdl)
{
    assert(hdl != 0);
    assert(sizeof(jlong) >= sizeof(const MeCab::Node*));
    return reinterpret_cast<const MeCab::Node*>(hdl);
}

inline jlong node2hdl(const MeCab::Node* p)
{
    assert(p != NULL);
    assert(sizeof(jlong) >= sizeof(const MeCab::Node*));
    return reinterpret_cast<jlong>(p);
}


/**
 * Javaバイト配列をC++文字列に変換する。
 * @throw ArrayException 配列要素の取得に失敗
 */
inline std::string byteArrayToString(JNIEnv* env, jbyteArray arr)
{
    assert(sizeof(jbyte) == sizeof(char));

    ByteArrayHelper helper(env, arr, true);
    return std::string((const char*)helper.ptr(), (size_t)helper.length());
}

/**
 * C++文字列をJavaバイト配列に変換する。
 * @throw bad_alloc Javaバイト配列の確保に失敗
 * @throw ArrayException 配列要素の取得に失敗
 */
inline jbyteArray stringToByteArray(JNIEnv* env, const std::string& str)
{
    assert(sizeof(jbyte) == sizeof(char));

    ByteArrayHelper helper(env, (jsize)str.length());
    str.copy((char*)helper.ptr(), (size_t)helper.length());
    return helper.array();
}

inline jbyteArray stringToByteArray(JNIEnv* env, const char* p, size_t len)
{
    assert(sizeof(jbyte) == sizeof(char));

    ByteArrayHelper helper(env, (jsize)len);
    std::copy(p, p+sizeof(char)*len, helper.ptr());
    return helper.array();
}

#endif
