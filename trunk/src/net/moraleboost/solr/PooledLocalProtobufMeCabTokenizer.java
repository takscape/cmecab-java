package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import net.moraleboost.lucene.analysis.ja.LocalProtobufMeCabTokenizer;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;

import org.apache.commons.pool.ObjectPool;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

public class PooledLocalProtobufMeCabTokenizer extends Tokenizer
{
    private LocalProtobufMeCabTokenizer tokenizer = null;
    
    public PooledLocalProtobufMeCabTokenizer(Reader in, ObjectPool pool, int maxSize)
    throws Exception
    {
        super(in);
        
        Tagger tagger = null;
        try {
            tagger = (Tagger)pool.borrowObject();
            tokenizer = new LocalProtobufMeCabTokenizer(input, tagger, false, maxSize);
        } finally {
            if (tagger != null) {
                pool.returnObject(tagger);
            }
        }
    }

    @Override
    public Token next(Token reusableToken) throws MeCabException, IOException
    {
        return tokenizer.next(reusableToken);
    }
}
