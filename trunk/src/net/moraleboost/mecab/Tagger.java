package net.moraleboost.mecab;

import java.nio.charset.CharacterCodingException;

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
