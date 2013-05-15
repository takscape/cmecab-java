package net.moraleboost.solr;

import java.io.Reader;
import java.util.Map;

import net.moraleboost.lucene.analysis.ja.VerbatimTokenizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;

public class VerbatimTokenizerFactory extends TokenizerFactory
{
    private int bufferSize = VerbatimTokenizer.DEFAULT_BUFFER_SIZE;

    public VerbatimTokenizerFactory(Map<String, String> args)
    {
        super(args);
        init(args);
    }
    
    protected void init(Map<String, String> args)
    {
        String argBufferSize = args.get("bufferSize");
        if (argBufferSize != null) {
            bufferSize = Integer.parseInt(argBufferSize);
        }
    }

    @Override
    public Tokenizer create(AttributeSource.AttributeFactory factory, Reader input)
    {
        return new VerbatimTokenizer(factory, input, bufferSize);
    }
}
