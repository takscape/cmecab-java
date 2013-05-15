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

import net.moraleboost.lucene.analysis.ja.CJKTokenizer2;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;

import java.io.Reader;
import java.util.Map;

/**
 * {@link CJKTokenizer2}のファクトリ。
 * @author taketa
 *
 */
public class CJKTokenizer2Factory extends TokenizerFactory
{
    private int ngram = CJKTokenizer2.DEFAULT_NGRAM;
    
    /**
     * パラメータ「ngram」を取る。ngramには、N-gramのNを指定。
     * 1を指定すると、unigram。2を指定すると、bigram、3を指定するとtrigram。
     * 省略した場合のデフォルトは{@value CJKTokenizer2#DEFAULT_NGRAM}である。
     */
    public CJKTokenizer2Factory(Map<String, String> args)
    {
        super(args);
        init(args);
    }

    protected void init(Map<String, String> args)
    {
        String argNgram = args.get("ngram");
        if (argNgram != null) {
            ngram = Integer.parseInt(argNgram);
        }
    }
    
    public int getNgram()
    {
        return ngram;
    }

    @Override
    public Tokenizer create(AttributeSource.AttributeFactory factory, Reader input)
    {
        return new CJKTokenizer2(input, ngram);
    }
}
