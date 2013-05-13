package net.moraleboost.solr;

import net.moraleboost.lucene.analysis.ja.TermRegexFilter;
import org.apache.lucene.analysis.TokenStream;

public class TermRegexFilterFactory extends AbstractRegexFilterFactory
{
    public TermRegexFilterFactory()
    {
        super();
    }

    public TokenStream create(TokenStream input)
    {
        return new TermRegexFilter(input, getStopPattern());
    }
}
