package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;

import org.apache.lucene.analysis.TokenStream;

public class CharStreamAwareStandardMeCabTokenizerFactory extends
        StandardMeCabTokenizerFactory
{
    public CharStreamAwareStandardMeCabTokenizerFactory()
    {
        super();
    }

    public TokenStream create(Reader reader)
    {
        try {
            return new CharStreamAwareStandardMeCabTokenizer(
                    reader, getDicCharset(), getMecabArg(), getMaxSize());
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
