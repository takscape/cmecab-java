/*
 **
 **  Feb. 17, 2009
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

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenizerFactory;

import net.moraleboost.lucene.analysis.ja.CJKTokenizer2;

/**
 * {@link CJKTokenizer2}のファクトリ。
 * @author taketa
 *
 */
public class CJKTokenizer2Factory extends BaseTokenizerFactory
{
    private int ngram = CJKTokenizer2.DEFAULT_NGRAM;
    
    public CJKTokenizer2Factory()
    {
        super();
    }
    
    public int getNgram()
    {
        return ngram;
    }

    /**
     * パラメータ「ngram」を取る。ngramには、N-gramのNを指定。
     * 1を指定すると、unigram。2を指定すると、bigram、3を指定するとtrigram。
     * 省略した場合のデフォルトは{@value CJKTokenizer2#DEFAULT_NGRAM}である。
     */
    public void init(Map<String, String> args)
    {
        super.init(args);

        String argNgram = args.get("ngram");
        if (argNgram != null) {
            ngram = Integer.parseInt(argNgram);
        }
    }

    public TokenStream create(Reader in)
    {
        return new CJKTokenizer2(in, ngram);
    }
}
