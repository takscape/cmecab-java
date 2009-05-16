package net.moraleboost.mecab;

import java.nio.charset.CharacterCodingException;
import java.util.Iterator;

public interface Node extends Iterator<String>
{
    /**
     * Nodeを閉じる。close()呼び出し以降にNodeのメソッドを呼び出すと、結果は未定義である。
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
     * 現在の位置の形態素の表層形を返す。
     * 
     * @return 表層形
     */
    public abstract String surface()
    throws CharacterCodingException, MeCabException;

    /**
     * 先頭の空白も含めて形態素の表層形を返す。
     * 
     * @return 先頭の空白も含めた表層形
     */
    public abstract String rsurface()
    throws CharacterCodingException, MeCabException;
}
