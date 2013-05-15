package net.moraleboost.solr;

import net.moraleboost.lucene.analysis.ja.JapaneseNormalizationFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

public class JapaneseNormalizationFilterFactory extends TokenFilterFactory
{
    private boolean normAscii = true;
    private boolean normKana = true;
    private boolean normBlank = true;

    public JapaneseNormalizationFilterFactory(Map<String, String> args)
    {
        super(args);
        init(args);
    }

    protected void init(Map<String, String> args)
    {
        String ascii = args.get("normAscii");
        String kana = args.get("normKana");
        String blank = args.get("normBlank");

        if (ascii != null) {
            normAscii = Boolean.parseBoolean(ascii);
        }

        if (kana != null) {
            normKana = Boolean.parseBoolean(kana);
        }

        if (blank != null) {
            normBlank = Boolean.parseBoolean(blank);
        }
    }

    public TokenStream create(TokenStream tokenStream)
    {
        return new JapaneseNormalizationFilter(tokenStream, normAscii, normKana, normBlank);
    }
}
