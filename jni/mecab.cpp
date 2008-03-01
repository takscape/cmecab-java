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
#include "net_moraleboost_mecab_Tagger.h"
#include "net_moraleboost_mecab_Node.h"
#include <iostream>


extern "C" {

////////////////////////////////////////////////////////////////////
// net.moraleboost.mecab.Tagger
////////////////////////////////////////////////////////////////////
/*
 * Class:     net_moraleboost_mecab_Tagger
 * Method:    _create
 * Signature: ([B)J
 */
JNIEXPORT jlong JNICALL
Java_net_moraleboost_mecab_Tagger__1create(
    JNIEnv* env,
    jclass clazz,
    jbyteArray arg
    )
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

/*
 * Class:     net_moraleboost_mecab_Tagger
 * Method:    _destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL
Java_net_moraleboost_mecab_Tagger__1destroy(
    JNIEnv* env,
    jclass clazz,
    jlong hdl
    )
{
    try {
        TaggerHelper* tagger = hdl2tagger(hdl);
        delete tagger;
    } catch (...) {}
}

/*
 * Class:     net_moraleboost_mecab_Tagger
 * Method:    _parse
 * Signature: (J[B)J
 */
JNIEXPORT jlong JNICALL
Java_net_moraleboost_mecab_Tagger__1parse(
    JNIEnv* env,
    jclass clazz,
    jlong hdl,
    jbyteArray text
    )
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

/*
 * Class:     net_moraleboost_mecab_Tagger
 * Method:    _version
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_net_moraleboost_mecab_Tagger__1version(
    JNIEnv* env,
    jclass clazz
    )
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
// net.moraleboost.mecab.Node
////////////////////////////////////////////////////////////////////
/*
 * Class:     net_moraleboost_mecab_Node
 * Method:    _surface
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL
Java_net_moraleboost_mecab_Node__1surface(
    JNIEnv* env,
    jclass clazz,
    jlong hdl
    )
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

/*
 * Class:     net_moraleboost_mecab_Node
 * Method:    _feature
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL
Java_net_moraleboost_mecab_Node__1feature(
    JNIEnv* env,
    jclass clazz,
    jlong hdl
    )
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

/*
 * Class:     net_moraleboost_mecab_Node
 * Method:    _next
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL
Java_net_moraleboost_mecab_Node__1next(
    JNIEnv* env,
    jclass clazz,
    jlong hdl
    )
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
