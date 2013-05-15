/*
 **
 **  Mar. 22, 2008
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

import net.moraleboost.lucene.analysis.ja.FeatureRegexFilter;
import org.apache.lucene.analysis.TokenStream;

import java.util.Map;

/**
 * {@link FeatureRegexFilter}のファクトリ。
 * @author taketa
 *
 */
public class FeatureRegexFilterFactory extends AbstractRegexFilterFactory
{
    public FeatureRegexFilterFactory(Map<String, String> args)
    {
        super(args);
    }

    public TokenStream create(TokenStream input)
    {
        return new FeatureRegexFilter(input, getStopPattern());
    }
}
