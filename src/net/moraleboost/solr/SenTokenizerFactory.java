package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenizerFactory;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;
import net.moraleboost.lucene.analysis.ja.SenTokenizer;

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
    
    public void init(Map<String, String> args)
    {
        String confFile = args.get("confFile");
        if (confFile == null) {
            throw new MeCabTokenizerException("confFile is not specified.");
        } else {
            this.confFile = confFile;
        }
    }

    public TokenStream create(Reader reader)
    {
        try {
            return new SenTokenizer(reader, confFile);
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
