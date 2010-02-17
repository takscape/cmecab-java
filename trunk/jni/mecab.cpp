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
#include <iostream>


extern "C" {

////////////////////////////////////////////////////////////////////
// net.moraleboost.mecab.impl.StandardTagger
////////////////////////////////////////////////////////////////////
JNIEXPORT jlong JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1create
  (JNIEnv *env, jclass clazz, jbyteArray arg)
{
    return createTagger(env, clazz, arg);
}

JNIEXPORT void JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1destroy
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    destroyTagger(env, clazz, hdl);
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
  (JNIEnv *env, jclass clazz, jlong hdl)
{
    TaggerHelper* tagger = hdl2tagger(hdl);
    const MeCab::Node* node = tagger->firstNode();
    return node2hdl(node);
}

JNIEXPORT jbyteArray JNICALL Java_net_moraleboost_mecab_impl_StandardTagger__1version
  (JNIEnv *env, jclass clazz)
{
    return version(env, clazz);
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

} // end of extern "C"
