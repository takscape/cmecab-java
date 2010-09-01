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
package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.solr.analysis.BaseTokenizerFactory;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;
import net.moraleboost.lucene.analysis.ja.SenTokenizer;

/**
 * {@link SenTokenizer}のファクトリ。
 * @author taketa
 *
 */
public class SenTokenizerFactory extends BaseTokenizerFactory
{
    private String confFile = null;
    
    public SenTokenizerFactory()
    {
        super();
    }
    
    public String getConfFile()
    {
        return confFile;
    }
    
    /**
     * パラメータ「confFile」を取る。このパラメータは必須。
     * Senの設定ファイル（sen.xml）のパスを指定する。
     */
    public void init(Map<String, String> args)
    {
        String confFile = args.get("confFile");
        if (confFile == null) {
            throw new MeCabTokenizerException("confFile is not specified.");
        } else {
            this.confFile = confFile;
        }
    }

    public Tokenizer create(Reader reader)
    {
        try {
            return new SenTokenizer(reader, confFile);
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
