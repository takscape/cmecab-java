package net.moraleboost.solr;

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenizerFactory;

import net.moraleboost.lucene.analysis.ja.CJKTokenizer2;


public class CJKTokenizer2Factory extends BaseTokenizerFactory
{
    public CJKTokenizer2Factory()
    {
        super();
    }
    
    public void init(Map<String, String> args)
    {
        super.init(args);
        // 初期化項目なし
    }

    public TokenStream create(Reader in)
    {
        return new CJKTokenizer2(in);
    }
}
