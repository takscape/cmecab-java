package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;

import org.apache.lucene.analysis.TokenStream;

public class CharStreamAwareLocalProtobufMeCabTokenizerFactory extends
        LocalProtobufMeCabTokenizerFactory
{
    public CharStreamAwareLocalProtobufMeCabTokenizerFactory()
    {
        super();
    }
    
    public TokenStream create(Reader reader)
    {
        try {
            return new CharStreamAwareLocalProtobufMeCabTokenizer(
                    reader, getMecabArg(), getMaxSize());
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
