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

import java.io.Reader;
import java.util.Map;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.StandardTagger;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.lucene.analysis.TokenStream;

public class PooledStandardMeCabTokenizerFactory extends StandardMeCabTokenizerFactory
{
    private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;
    private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;
    
    private GenericObjectPool pool = null; // Taggerのプール
    
    public PooledStandardMeCabTokenizerFactory()
    {
        super();
    }
    
    public int getMaxActive()
    {
        return maxActive;
    }
    
    public long getMaxWait()
    {
        return maxWait;
    }
    
    public void init(Map<String, String> args)
    {
        super.init(args);
        
        // 追加でpoolのパラメータを読む
        String maxActiveArg = args.get("maxActive");
        String maxWaitArg = args.get("maxWait");
        
        if (maxActiveArg != null) {
            maxActive = Integer.parseInt(maxActiveArg);
        }
        
        if (maxWaitArg != null) {
            maxWait = Long.parseLong(maxWaitArg);
        }
        
        // poolを作成
        createObjectPool();
    }
    
    private void createObjectPool()
    {
        pool = new GenericObjectPool();
        pool.setMaxActive(getMaxActive());
        pool.setMaxWait(getMaxWait());
        
        final String charset = getDicCharset();
        final String arg = getMecabArg();
        
        pool.setFactory(new BasePoolableObjectFactory() {
            @Override
            public Object makeObject() throws Exception
            {
                return new StandardTagger(charset, arg);
            }

            @Override
            public void destroyObject(Object obj)
            {
                if (obj != null) {
                    Tagger tagger = (Tagger)obj;
                    tagger.close();
                }
            }
        });
    }

    public TokenStream create(Reader reader)
    {
        try {
            return new PooledStandardMeCabTokenizer(reader, pool, getMaxSize());
        } catch (MeCabTokenizerException e) {
            throw e;
        } catch (Exception e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
