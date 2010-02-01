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
#include "internal.h"
#include "net_moraleboost_mecab_impl_StandardTagger.h"
#include "net_moraleboost_mecab_impl_StandardNode.h"
#include "net_moraleboost_mecab_impl_LocalProtobufTagger.h"
#include "messages.pb.h"
#include <iostream>


extern "C" {

////////////////////////////////////////////////////////////////////
// net.moraleboost.mecab.impl.StandardTagger
////////////////////////////////////////////////////////////////////
JNIEXPORT jlong JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1create
  (JNIEnv *env, jclass clazz, jbyteArray arg)
{
    DECLARE_CLASS(oomError, env, CLASS_OUT_OF_MEMORY_ERROR, 0);

    try {
        std::string argstr = byteArrayToString(env, arg);
        TaggerHelper* tagger = new TaggerHelper(argstr.c_str());
        return tagger2hdl(tagger);
    } catch (std::bad_alloc&) {
        env->ThrowNew(oomError, "Can't allocate a tagger.");
    } catch (...) {}

    return 0;
}

JNIEXPORT void JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1destroy
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    try {
        TaggerHelper* tagger = hdl2tagger(hdl);
        delete tagger;
    } catch (...) {}
}

JNIEXPORT jlong JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1parse
  (JNIEnv *env, jclass clazz, jlong hdl, jbyteArray text)
{
    DECLARE_CLASS(oomError, env, CLASS_OUT_OF_MEMORY_ERROR, 0);
    DECLARE_CLASS(mecabException, env, CLASS_MECAB_EXCEPTION, 0);

    try {
        ByteArrayHelper helper(env, text, true);
        TaggerHelper* tagger = hdl2tagger(hdl);
        const MeCab::Node* node = tagger->parse((const char*)helper.ptr(),
                                                (size_t)helper.length());
        return node2hdl(node);
    } catch (ArrayException&) {
        env->ThrowNew(mecabException, "Can't access array elements.");
    } catch (std::bad_alloc&) {
        env->ThrowNew(oomError, "Can't parse text.");
    } catch (...) {
        // unknown error
    }

    return 0;
}

JNIEXPORT jlong JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1firstNode
  (JNIEnv *, jclass, jlong)
{
    TaggerHelper* tagger = hdl2tagger(hdl);
    const MeCab::Node* node = tagger->firstNode();
    return node2hdl(node);
}

JNIEXPORT jbyteArray JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1version
  (JNIEnv *env, jclass clazz)
{
    DECLARE_CLASS(oomError, env, CLASS_OUT_OF_MEMORY_ERROR, 0);
    DECLARE_CLASS(mecabException, env, CLASS_MECAB_EXCEPTION, 0);

    try {
        return stringToByteArray(env, mecab_version());
    } catch (std::bad_alloc&) {
        env->ThrowNew(oomError, "Can't get version.");
    } catch (...) {
        env->ThrowNew(mecabException, "Can't get version.");
    }

    return 0;
}

////////////////////////////////////////////////////////////////////
// net.moraleboost.mecab.impl.StandardNode
////////////////////////////////////////////////////////////////////
JNIEXPORT jbyteArray JNICALL Java_net_moraleboost_mecab_impl_StandardNode__1surface
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    DECLARE_CLASS(oomError, env, CLASS_OUT_OF_MEMORY_ERROR, 0);
    DECLARE_CLASS(mecabException, env, CLASS_MECAB_EXCEPTION, 0);

    try {
        const MeCab::Node* node = hdl2node(hdl);
        return stringToByteArray(env, node->surface, (size_t)(node->length));
    } catch (ArrayException&) {
        env->ThrowNew(mecabException, "Can't access array elements.");
    } catch (std::bad_alloc&) {
        env->ThrowNew(oomError, "Can't get surface.");
    } catch (...) {
        env->ThrowNew(mecabException, "Unknown error.");
    }

    return 0;
}

JNIEXPORT jbyteArray JNICALL Java_net_moraleboost_mecab_impl_StandardNode__1blank
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    DECLARE_CLASS(oomError, env, CLASS_OUT_OF_MEMORY_ERROR, 0);
    DECLARE_CLASS(mecabException, env, CLASS_MECAB_EXCEPTION, 0);

    try {
        const MeCab::Node* node = hdl2node(hdl);
        if (node->length == node->rlength) {
            return 0;
        } else {
            size_t blanklen = (size_t)(node->rlength - node->length);
            const char* rsurface = node->surface - blanklen;
            return stringToByteArray(env, rsurface, blanklen);
        }
    } catch (ArrayException&) {
        env->ThrowNew(mecabException, "Can't access array elements.");
    } catch (std::bad_alloc&) {
        env->ThrowNew(oomError, "Can't get rsurface.");
    } catch (...) {
        env->ThrowNew(mecabException, "Unknown error.");
    }

    return 0;
}

JNIEXPORT jbyteArray JNICALL Java_net_moraleboost_mecab_impl_StandardNode__1feature
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    DECLARE_CLASS(oomError, env, CLASS_OUT_OF_MEMORY_ERROR, 0);
    DECLARE_CLASS(mecabException, env, CLASS_MECAB_EXCEPTION, 0);

    try {
        const MeCab::Node* node = hdl2node(hdl);
        return stringToByteArray(env, node->feature);
    } catch (ArrayException&) {
        env->ThrowNew(mecabException, "Can't access array elements.");
    } catch (std::bad_alloc&) {
        env->ThrowNew(oomError, "Can't get feature.");
    } catch (...) {
        env->ThrowNew(mecabException, "Unknown error.");
    }

    return 0;
}

JNIEXPORT jint JNICALL Java_net_moraleboost_mecab_impl_StandardNode__1posid
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    const MeCab::Node* node = hdl2node(hdl);
    return (jint)(node->posid);
}

JNIEXPORT jlong JNICALL Java_net_moraleboost_mecab_impl_StandardNode__1next
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    if (hdl == 0) {
        return 0;
    }

    DECLARE_CLASS(mecabException, env, CLASS_MECAB_EXCEPTION, 0);

    try {
        const MeCab::Node* node = hdl2node(hdl);
        node = node->next;
        if (!node || node->stat == MECAB_EOS_NODE) {
            return 0;
        } else {
            return node2hdl(node);
        }
    } catch (...) {
        env->ThrowNew(mecabException, "Unknown error.");
    }

    return 0;
}

////////////////////////////////////////////////////////////////////
// net.moraleboost.mecab.impl.LocalProtobufTagger
////////////////////////////////////////////////////////////////////
JNIEXPORT jlong JNICALL Java_net_moraleboost_mecab_impl_LocalProtobufTagger__1create
  (JNIEnv *env, jclass clazz, jbyteArray arg)
{
    return Java_net_moraleboost_mecab_impl_StandardTagger__1create(env, clazz, arg);
}

JNIEXPORT void JNICALL Java_net_moraleboost_mecab_impl_LocalProtobufTagger__1destroy
  (JNIEnv *env, jclass clazz, jlong arg)
{
    Java_net_moraleboost_mecab_impl_StandardTagger__1destroy(env, clazz, arg);
}

JNIEXPORT jbyteArray JNICALL Java_net_moraleboost_mecab_impl_LocalProtobufTagger__1parse
  (JNIEnv *env, jclass clazz, jlong hdl, jbyteArray requestdata)
{
    using namespace net::moraleboost::mecab::impl;

    DECLARE_CLASS(oomError, env, CLASS_OUT_OF_MEMORY_ERROR, 0);
    DECLARE_CLASS(mecabException, env, CLASS_MECAB_EXCEPTION, 0);

    ByteArrayHelper helper(env, requestdata, true);
    ParsingRequest request;

    try {
        if (!request.ParseFromArray(helper.ptr(), helper.length())) {
            // failed to parse protobuf message
            env->ThrowNew(mecabException, "Can't parse ParsingRequest.");
        } else {
            ParsingResponse response;
            ParsingResponse_Morpheme* morpheme;

            std::string text = request.text();
            TaggerHelper* tagger = hdl2tagger(hdl);
            const MeCab::Node* node = tagger->parse(text.c_str(), text.length());
            node = node->next;
            while (node && node->stat != MECAB_EOS_NODE) {
                morpheme = response.add_morpheme();
                morpheme->set_surface(node->surface, (size_t)(node->length));
                if (node->length != node->rlength) {
                    size_t blanklen = (size_t)(node->rlength - node->length);
                    morpheme->set_blank(node->surface - blanklen, blanklen);
                }
                morpheme->set_feature(node->feature);
                morpheme->set_posid(node->posid);

                node = node->next;
            }

            std::string out;
            if (!response.SerializeToString(&out)) {
                env->ThrowNew(mecabException, "Can't write ParsingResponse."); 
            } else {
                return stringToByteArray(env, out);
            }
        }
    } catch (ArrayException&) {
        env->ThrowNew(mecabException, "Can't access array elements.");
    } catch (std::bad_alloc&) {
        env->ThrowNew(oomError, "Can't parse text.");
    } catch (...) {
        // unknown error
    }

    return 0;
}

JNIEXPORT jbyteArray JNICALL Java_net_moraleboost_mecab_impl_LocalProtobufTagger__1version
  (JNIEnv *env, jclass clazz)
{
    return Java_net_moraleboost_mecab_impl_StandardTagger__1version(env, clazz);
}

} // end of extern "C"
