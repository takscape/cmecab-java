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

import net.moraleboost.mecab.Tagger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * {@link StandardMeCabTokenizer}を用いて入力を分かち書きするAnalyzer。
 * 
 * @author taketa
 *
 */
public class StandardMeCabAnalyzer extends Analyzer
{
    private Tagger tagger;
    private int maxSize;
    private String[] stopPatterns;

    /**
     * StandardMeCabTokenizerをTokenizerとして用いるAnalyzerを構築する。
     * 
     * @param tagger
     *            Taggerインスタンス
     * @param maxSize
     *            入力から読み込む最大文字数(in chars)
     */
    public StandardMeCabAnalyzer(Tagger tagger, int maxSize)
    {
        this(tagger, maxSize, null);
    }

    /**
     * StandardMeCabTokenizerによって分かち書きされたトークンを
     * FeatureRegexFilterによってフィルタリングするAnalyzerを構築する。
     * 
     * @param tagger
     *            Taggerインスタンス
     * @param maxSize
     *            入力から読み込む最大文字数(in chars)
     * @param stopPatterns
     *            FeatureRegexFilterに与える正規表現の配列
     */
    public StandardMeCabAnalyzer(Tagger tagger, int maxSize, String[] stopPatterns)
    {
        super();
        this.tagger = tagger;
        this.maxSize = maxSize;
        this.stopPatterns = stopPatterns;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        try {
            Tokenizer tokenizer =
                    new StandardMeCabTokenizer(reader, tagger, maxSize);

            if (stopPatterns != null) {
                return new TokenStreamComponents(tokenizer,
                        new FeatureRegexFilter(tokenizer, stopPatterns));
            } else {
                return new TokenStreamComponents(tokenizer);
            }
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
