/*
 **
 **  May. 17, 2009
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

import net.moraleboost.lucene.analysis.ja.LocalProtobufMeCabTokenizer;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.LocalProtobufTagger;

import org.apache.commons.pool.ObjectPool;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

/**
 * {@link LocalProtobufTagger}をプールして用いるTokenizer。
 * @author taketa
 *
 */
public class PooledLocalProtobufMeCabTokenizer extends Tokenizer
{
    private LocalProtobufMeCabTokenizer tokenizer = null;
    
    /**
     * オブジェクトを構築する。
     * 
     * @param in 入力
     * @param pool LocalProtobufTaggerのプール
     * @param maxSize LocalProtobufMeCabTokenizerのコンストラクタと同じ
     * @throws Exception
     */
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
