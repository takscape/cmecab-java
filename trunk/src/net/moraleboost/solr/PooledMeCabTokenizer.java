package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.pool.ObjectPool;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizer;
import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;
import net.moraleboost.mecab.Tagger;

public class PooledMeCabTokenizer extends MeCabTokenizer
{
    private ObjectPool pool = null;
    
    public PooledMeCabTokenizer(Reader in, ObjectPool pool,
            int initialSize, int shrinkThreshold, int shrinkTarget, int maxSize)
    throws Exception
    {
        super(in, (Tagger)pool.borrowObject(),
                initialSize, shrinkThreshold, shrinkTarget, maxSize);
        this.pool = pool;
    }

    @Override
    public void close() throws IOException
    {
        super.close();

        try {
            Tagger tagger = getTagger();
            if (tagger != null) {
                pool.returnObject(tagger);
            }
        } catch (Exception e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
