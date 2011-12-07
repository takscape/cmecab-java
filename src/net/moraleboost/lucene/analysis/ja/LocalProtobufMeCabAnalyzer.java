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
package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

/**
 * {@link LocalProtobufMeCabTokenizer}をTokenizerとして用いるAnalyzer
 * @author taketa
 *
 */
public class LocalProtobufMeCabAnalyzer extends Analyzer
{
    private String mecabArg = null;
    private String[] stopPatterns = null;

    /**
     * LocalProtobufMeCabTokenizerをTokenizerとして用いるAnalyzerを構築する。
     * 
     * @param mecabArg
     *            MeCabに与えるオプション
     */
    public LocalProtobufMeCabAnalyzer(String mecabArg)
    {
        super();
        this.mecabArg = mecabArg;
    }

    /**
     * LocalProtobufMeCabTokenizerによって分かち書きされたトークンを
     * FeatureRegexFilterによってフィルタリングするAnalyzerを構築する。
     * 
     * @param mecabArg
     *            MeCabに与えるオプション
     * @param stopPatterns
     *            FeatureRegexFilterに与える正規表現の配列
     */
    public LocalProtobufMeCabAnalyzer(String mecabArg,
            String[] stopPatterns)
    {
        super();
        this.mecabArg = mecabArg;
        this.stopPatterns = stopPatterns;
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
        try {
            TokenStream stream = new LocalProtobufMeCabTokenizer(reader, mecabArg);

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
            
            LocalProtobufMeCabTokenizer tokenizer =
                new LocalProtobufMeCabTokenizer(reader, mecabArg);
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
        public LocalProtobufMeCabTokenizer tokenizer;
        public FeatureRegexFilter filter;
    }
}
