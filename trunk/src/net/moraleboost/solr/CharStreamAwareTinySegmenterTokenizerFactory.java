package net.moraleboost.solr;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;

public class CharStreamAwareTinySegmenterTokenizerFactory extends
        TinySegmenterTokenizerFactory
{
    public CharStreamAwareTinySegmenterTokenizerFactory()
    {
        super();
    }

    public TokenStream create(Reader in)
    {
        return new CharStreamAwareTinySegmenterTokenizer(in);
    }
}
