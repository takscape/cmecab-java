package net.moraleboost.solr;

import java.io.Reader;
import java.util.Map;

import net.moraleboost.lucene.analysis.ja.TinySegmenterTokenizer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenizerFactory;

public class TinySegmenterTokenizerFactory extends BaseTokenizerFactory
{
    public TinySegmenterTokenizerFactory()
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
        return new TinySegmenterTokenizer(in);
    }
}
