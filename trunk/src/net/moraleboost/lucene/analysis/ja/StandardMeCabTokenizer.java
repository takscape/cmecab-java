/*
 **
 **  Mar. 5, 2008
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
package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;
import java.io.IOException;

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.StandardTagger;

public class StandardMeCabTokenizer extends MeCabTokenizer
{
    /**
     * MeCabを用いて入力を分かち書きするTokenizerを構築する。
     * 
     * @param in
     *            入力
     * @param dicCharset
     *            MeCabの辞書の文字コード
     * @param arg
     *            MeCabに与えるオプション
     * @throws MeCabException
     * @throws IOException
     */
    public StandardMeCabTokenizer(Reader in, String dicCharset, String arg)
    throws MeCabException, IOException
    {
        this(in, dicCharset, arg, DEFAULT_MAX_SIZE);
    }

    /**
     * MeCabを用いて入力を分かち書きするTokenizerを構築する。
     * 
     * @param in
     *            入力
     * @param dicCharset
     *            MeCabの辞書の文字コード
     * @param arg
     *            MeCabに与えるオプション
     * @param maxSize
     *            入力から読み込んだデータの量がこの値を超えると、
     *            解析は失敗し、MeCabExceptionが発生する。
     * @throws IOException
     * @throws MeCabException
     */
    public StandardMeCabTokenizer(Reader in, String dicCharset, String arg, int maxSize)
    throws MeCabException, IOException
    {
        this(in, new StandardTagger(dicCharset, arg), true, maxSize);
    }
    
    public StandardMeCabTokenizer(Reader in, Tagger tagger, boolean ownTagger, int maxSize)
    throws MeCabException, IOException
    {
        super(in, tagger, ownTagger, maxSize);
    }
}
