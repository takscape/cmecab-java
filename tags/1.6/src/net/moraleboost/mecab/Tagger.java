/*
 **
 **  May. 17, 2009
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
package net.moraleboost.mecab;

import java.nio.charset.CharacterCodingException;

/**
 * 形態素解析器を表すインターフェース。
 * 
 * @author taketa
 *
 */
public interface Tagger
{
    /**
     * 形態素解析器を破棄し、リソースを解放する。
     * 以降、parse()の呼び出しにより返された既存のNodeは、
     * 正常に動作することが保証されない。
     */
    public abstract void close();

    /**
     * 形態素解析を行う。
     * 
     * @param text
     *            解析対象文字列
     * @return Nodeオブジェクト
     * @throws CharacterCodingException
     *             textをバイト列にエンコードできなかった
     * @throws MeCabException
     *             ネイティブライブラリの内部エラー
     */
    public abstract Node parse(CharSequence text)
            throws CharacterCodingException, MeCabException;
}
