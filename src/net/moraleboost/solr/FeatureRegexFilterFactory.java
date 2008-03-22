/*
**
**  Mar. 22, 2008
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

import java.util.ArrayList;
import java.util.Map;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

import net.moraleboost.lucene.analysis.ja.FeatureRegexFilter;
import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;

public class FeatureRegexFilterFactory extends BaseTokenFilterFactory
{
    private String[] stopPattern = null;
    
    public FeatureRegexFilterFactory()
    {
        super();
    }

    /**
     * ファクトリを初期化する。 初期化パラメータとして、「source」及び「charset」をとる。
     * 「source」には、一行に一つ正規表現を記述したファイルの名前を指定する。
     * 「charset」には、sourceで指定したファイルの文字コードを指定する。
     * いずれのパラメータも省略不可。
     * 
     * @param args 初期化パラメータ
     */
    public void init(Map<String, String> args)
    {
        String source = args.get("source");
        String charset = args.get("charset");
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        ArrayList<String> regexList = new ArrayList<String>();
        String regex = null;

        try {
            fis = new FileInputStream(source);
            isr = new InputStreamReader(fis, charset);
            br = new BufferedReader(isr);
            while ((regex = br.readLine()) != null) {
                if (!regex.isEmpty()) {
                    regexList.add(regex);
                }
            }
            stopPattern = regexList.toArray(new String[regexList.size()]);
        } catch (Exception e) {
            throw new MeCabTokenizerException("Can't read regex source.", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {}
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {}
            }
        }
    }

    public TokenStream create(TokenStream input)
    {
        return new FeatureRegexFilter(input, stopPattern);
    }
}
