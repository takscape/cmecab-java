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
}
