package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class TinySegmenterAnalyzer extends Analyzer
{
    public TinySegmenterAnalyzer()
    {
        super();
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
        return new TinySegmenterTokenizer(reader);
    }
    
    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader)
    throws IOException
    {
        TinySegmenterTokenizer tokenizer =
            (TinySegmenterTokenizer)getPreviousTokenStream();
        if (tokenizer == null) {
            tokenizer = new TinySegmenterTokenizer(reader);
            setPreviousTokenStream(tokenizer);
        } else {
            tokenizer.reset(reader);
        }
        
        return tokenizer;
    }
}
