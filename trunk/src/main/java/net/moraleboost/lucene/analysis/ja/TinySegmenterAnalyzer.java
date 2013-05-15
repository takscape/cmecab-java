package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;

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
