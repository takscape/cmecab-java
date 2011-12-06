package net.moraleboost.solr;

import net.moraleboost.lucene.analysis.ja.JapaneseNormalizationFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

import java.util.Map;

public class JapaneseNormalizationFilterFactory extends BaseTokenFilterFactory
{
    private boolean normFullWidth = true;
    private boolean normHalfWidth = true;
    private boolean normBlank = true;

    public JapaneseNormalizationFilterFactory()
    {
        super();
    }

    public void init(Map<String, String> args)
    {
        String full = args.get("normFullWidth");
        String half = args.get("normHalfWidth");
        String blank = args.get("normBlank");

        if (full != null) {
            normFullWidth = Boolean.parseBoolean(full);
        }

        if (half != null) {
            normHalfWidth = Boolean.parseBoolean(half);
        }

        if (blank != null) {
            normBlank = Boolean.parseBoolean(blank);
        }
    }

    public TokenStream create(TokenStream tokenStream)
    {
        return new JapaneseNormalizationFilter(tokenStream, normFullWidth, normHalfWidth, normBlank);
    }
}
