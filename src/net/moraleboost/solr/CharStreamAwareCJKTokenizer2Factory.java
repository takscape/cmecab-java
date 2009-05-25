package net.moraleboost.solr;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;

public class CharStreamAwareCJKTokenizer2Factory extends CJKTokenizer2Factory
{
    public CharStreamAwareCJKTokenizer2Factory()
    {
        super();
    }

    public TokenStream create(Reader in)
    {
        return new CharStreamAwareCJKTokenizer2(in, getNgram());
    }
}
