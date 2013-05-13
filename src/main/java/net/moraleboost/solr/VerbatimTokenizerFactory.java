package net.moraleboost.solr;

import java.io.Reader;
import java.util.Map;

import net.moraleboost.lucene.analysis.ja.VerbatimTokenizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.solr.analysis.BaseTokenizerFactory;

public class VerbatimTokenizerFactory extends BaseTokenizerFactory
{
    private int bufferSize = VerbatimTokenizer.DEFAULT_BUFFER_SIZE;
    
    public void init(Map<String, String> args)
    {
        super.init(args);

        String argBufferSize = args.get("bufferSize");
        if (argBufferSize != null) {
            bufferSize = Integer.parseInt(argBufferSize);
        }
    }

    public Tokenizer create(Reader in)
    {
        return new VerbatimTokenizer(in, bufferSize);
    }
}
