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
 * {@link SenTokenizer}を用いて入力を分かち書きするAnalyzer。
 * 
 * @author taketa
 *
 */
public class SenAnalyzer extends Analyzer
{
    private String confFile = null;
    private String[] stopPatterns = null;

    /**
     * SenAnalyzerを構築する。
     * 
     * @param confFile Senの設定ファイル（sen.xml）のパス。
     */
    public SenAnalyzer(String confFile)
    {
        super();
        this.confFile = confFile;
    }
    
    /**
     * SenAnalyzerを構築する。
     * 
     * @param confFile Senの設定ファイル（sen.xml）のパス。
     * @param stopPattens FeatureRegexFilterに渡す正規表現パターンの配列。
     */
    public SenAnalyzer(String confFile, String[] stopPattens)
    {
        super();
        this.confFile = confFile;
        this.stopPatterns = stopPattens;
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
        try {
            TokenStream stream = new SenTokenizer(reader, this.confFile);

            if (stopPatterns != null) {
                stream = new FeatureRegexFilter(stream, stopPatterns);
            }

            return stream;
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
