package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;

import org.apache.lucene.analysis.TokenStream;

public class CharStreamAwareSenTokenizerFactory extends SenTokenizerFactory
{
    public CharStreamAwareSenTokenizerFactory()
    {
        super();
    }

    public TokenStream create(Reader reader)
    {
        try {
            return new CharStreamAwareSenTokenizer(reader, getConfFile());
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
