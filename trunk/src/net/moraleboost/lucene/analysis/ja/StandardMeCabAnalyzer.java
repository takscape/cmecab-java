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
import java.nio.charset.Charset;

import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.StandardTagger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

/**
 * {@link StandardMeCabTokenizer}を用いて入力を分かち書きするAnalyzer。
 * 
 * @author taketa
 *
 */
public class StandardMeCabAnalyzer extends Analyzer
{
    private Tagger tagger = null;
    private int maxSize = StandardMeCabTokenizer.DEFAULT_MAX_SIZE;
    private String[] stopPatterns = null;

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
        super();
        this.tagger = tagger;
        this.maxSize = maxSize;
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
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
        try {
            TokenStream stream =
                    new StandardMeCabTokenizer(reader, tagger, maxSize);

            if (stopPatterns != null) {
                stream = new FeatureRegexFilter(stream, stopPatterns);
            }

            return stream;
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
    
    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader)
    throws IOException
    {
        TokenStreamInfo info = (TokenStreamInfo)getPreviousTokenStream();
        
        if (info == null) {
            info = new TokenStreamInfo();
            
            StandardMeCabTokenizer tokenizer =
                new StandardMeCabTokenizer(reader, tagger, maxSize);
            info.tokenizer = tokenizer;
            
            if (stopPatterns != null) {
                info.filter = new FeatureRegexFilter(tokenizer, stopPatterns);;
            }
            
            setPreviousTokenStream(info);
        } else {
            if (info.filter != null) {
                info.filter.reset();
            }
            if (info.tokenizer != null) {
                info.tokenizer.reset(reader);
            }
        }

        if (info.filter != null) {
            return info.filter;
        } else {
            return info.tokenizer;
        }
    }
    
    private static class TokenStreamInfo
    {
        public StandardMeCabTokenizer tokenizer;
        public FeatureRegexFilter filter;
    }
}
