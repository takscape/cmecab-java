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
    private String dicCharset = null;
    private String mecabArg = null;
    private String[] stopPatterns = null;

    /**
     * StandardMeCabTokenizerをTokenizerとして用いるAnalyzerを構築する。
     * 
     * @param dicCharset
     *            MeCabの辞書の文字コード
     * @param mecabArg
     *            MeCabに与えるオプション
     */
    public StandardMeCabAnalyzer(String dicCharset, String mecabArg)
    {
        super();
        this.dicCharset = dicCharset;
        this.mecabArg = mecabArg;
    }

    /**
     * StandardMeCabTokenizerによって分かち書きされたトークンを
     * FeatureRegexFilterによってフィルタリングするAnalyzerを構築する。
     * 
     * @param dicCharset
     *            MeCabの辞書の文字コード
     * @param mecabArg
     *            MeCabに与えるオプション
     * @param stopPatterns
     *            FeatureRegexFilterに与える正規表現の配列
     */
    public StandardMeCabAnalyzer(String dicCharset, String mecabArg,
            String[] stopPatterns)
    {
        super();
        this.dicCharset = dicCharset;
        this.mecabArg = mecabArg;
        this.stopPatterns = stopPatterns;
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
        try {
            TokenStream stream = new StandardMeCabTokenizer(reader, dicCharset,
                    mecabArg);

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
                new StandardMeCabTokenizer(reader, dicCharset, mecabArg);
            info.tokenizer = tokenizer;
            
            if (stopPatterns != null) {
                FeatureRegexFilter filter =
                    new FeatureRegexFilter(tokenizer, stopPatterns);
                info.filter = filter;
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
