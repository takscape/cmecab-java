package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.analysis.Analyzer;

import java.io.Reader;

public class CJKAnalyzer2 extends Analyzer
{
    private int ngram;

    public CJKAnalyzer2()
    {
        this(CJKTokenizer2.DEFAULT_NGRAM);
    }

    public CJKAnalyzer2(int ngram)
    {
        super();
        this.ngram = ngram;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader)
    {
        return new TokenStreamComponents(new CJKTokenizer2(reader, ngram));
    }
}
