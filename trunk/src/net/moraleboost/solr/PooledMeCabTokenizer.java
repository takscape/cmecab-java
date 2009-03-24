/*
 **
 **  Mar. 24, 2009
 **
 **  The author disclaims copyright to this source code.
 **  In place of a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **
 **                                         Stolen from SQLite :-)
 **  Any feedback is welcome.
 **  Kohei TAKETA <k-tak@void.in>
 **
 */
package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.pool.ObjectPool;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizer;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;

public class PooledMeCabTokenizer extends Tokenizer
{
    private int bufferSize = MeCabTokenizer.DEFAULT_BUFFER_SIZE;
    private int maxSize = MeCabTokenizer.DEFAULT_MAX_SIZE;

    private List<Token> tokens = null;
    private Iterator<Token> iterator = null;
    
    public PooledMeCabTokenizer(Reader in, ObjectPool pool,
            int bufferSize, int maxSize)
    throws Exception
    {
        super(in);
        
        this.bufferSize = bufferSize;
        this.maxSize = maxSize;
        
        Tagger tagger = null;
        try {
            tagger = (Tagger)pool.borrowObject();
            parse(tagger);
        } finally {
            if (tagger != null) {
                pool.returnObject(tagger);
            }
        }
    }
    
    private void parse(Tagger tagger)
    throws MeCabException, IOException
    {
        MeCabTokenizer tokenizer = new MeCabTokenizer(
                input, tagger,
                bufferSize, maxSize);

        tokens = new LinkedList<Token>();
        
        Token token;
        while ((token = tokenizer.next()) != null) {
            tokens.add(token);
        }
        
        iterator = tokens.iterator();
    }

    @Override
    public Token next() throws MeCabException, IOException
    {
        if (iterator == null) {
            return null;
        }
        
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }
}
