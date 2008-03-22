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
