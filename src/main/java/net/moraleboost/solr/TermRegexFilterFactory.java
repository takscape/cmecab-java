package net.moraleboost.solr;

import net.moraleboost.lucene.analysis.ja.TermRegexFilter;
import org.apache.lucene.analysis.TokenStream;

import java.util.Map;

public class TermRegexFilterFactory extends AbstractRegexFilterFactory
{
    public TermRegexFilterFactory(Map<String, String> args)
    {
        super(args);
    }

    public TokenStream create(TokenStream input)
    {
        return new TermRegexFilter(input, getStopPattern());
    }
}
