package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.analysis.Analyzer;

import java.io.Reader;

public class TinySegmenterAnalyzer extends Analyzer
{
    public TinySegmenterAnalyzer()
    {
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader)
    {
        return new TokenStreamComponents(new TinySegmenterTokenizer(reader));
    }
}
