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

import net.moraleboost.lucene.analysis.ja.TinySegmenterTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;

import java.io.Reader;
import java.util.Map;

/**
 * {@link TinySegmenterTokenizer}のファクトリ。
 * @author taketa
 *
 */
public class TinySegmenterTokenizerFactory extends TokenizerFactory
{
    public TinySegmenterTokenizerFactory(Map<String, String> args)
    {
        super(args);
    }
    
    @Override
    public Tokenizer create(AttributeSource.AttributeFactory factory, Reader input)
    {
        return new TinySegmenterTokenizer(factory, input);
    }
}
