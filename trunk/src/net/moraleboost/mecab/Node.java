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
import java.util.Iterator;

/**
 * 形態素を表すインターフェース。
 * 
 * @author taketa
 *
 */
public interface Node extends Iterator<String>
{
    /**
     * Nodeを閉じる。
     * close()呼び出し以降にNodeのメソッドを呼び出した場合、結果は未定義である。
     */
    public abstract void close();

    /**
     * 次の形態素に移動し、その表層形を取得する。
     * 
     * @return 次の形態素の表層形
     * @throws CharacterCodingException
     *             表層形をJava文字列にデコードできなかった
     * @throws MeCabException
     *             ネイティブライブラリの内部エラー
     */
    public abstract String nextMorpheme()
    throws CharacterCodingException, MeCabException;

    /**
     * 現在の位置の形態素の素性情報を返す。
     * 
     * @return 素性情報
     * @throws CharacterCodingException
     *             素性情報をJava文字列にデコードできなかった
     * @throws MeCabException
     *             ネイティブライブラリ内部のエラー
     */
    public abstract String feature()
    throws CharacterCodingException, MeCabException;

    /**
     * 現在の位置の形態素の品詞IDを返す。
     * 
     * @return 品詞ID
     * @throws MeCabException
     *             ネイティブライブラリ内部のエラー
     */
    public abstract int posid()
    throws MeCabException;

    /**
     * 現在の位置の形態素の表層形を返す。
     * 
     * @return 表層形
     * @throws CharacterCodingException
     *             表層形をJava文字列にデコードできなかった
     * @throws MeCabException
     *             ネイティブライブラリ内部のエラー
     */
    public abstract String surface()
    throws CharacterCodingException, MeCabException;

    /**
     * surfaceに先立つ空白を返す。
     * 
     * @return surfaceに先立つ空白。空白が存在しなかった場合はnullを返す。
     * @throws CharacterCodingException
     *             空白をJava文字列にデコードできなかった
     * @throws MeCabException
     *             ネイティブライブラリ内部のエラー
     */
    public abstract String blank()
    throws CharacterCodingException, MeCabException;
}
