package net.moraleboost.solr;

import net.moraleboost.lucene.analysis.ja.VerbatimTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;

import java.io.Reader;
import java.util.Map;

public class VerbatimTokenizerFactory extends TokenizerFactory
{
    private int bufferSize;

    public VerbatimTokenizerFactory(Map<String, String> args)
    {
        super(args);
        bufferSize = VerbatimTokenizer.DEFAULT_BUFFER_SIZE;
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
