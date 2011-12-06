package net.moraleboost.solr;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;
import org.apache.solr.analysis.BaseTokenFilterFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractRegexFilterFactory extends BaseTokenFilterFactory
{
    private String[] stopPattern = null;

    public AbstractRegexFilterFactory()
    {
        super();
    }

    public String[] getStopPattern()
    {
        return stopPattern;
    }
    
    /**
     * ファクトリを初期化する。
     * 初期化パラメータとして、「source」及び「charset」をとる。
     * 「source」には、一行に一つ正規表現を記述したファイルの名前を指定する。
     * 「charset」には、sourceで指定したファイルの文字コードを指定する。
     * いずれのパラメータも省略不可。
     *
     * @param args
     *            初期化パラメータ
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
                if (!regex.equals("")) {
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
                } catch (Exception e) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
