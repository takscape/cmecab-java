package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public class FeatureRegexFilter extends TokenFilter
{
    private Pattern[] patterns = null;
    private Matcher[] matchers = null;

    public FeatureRegexFilter(TokenStream input, String[] stopPatterns)
    {
        super(input);
        buildPatterns(stopPatterns);
    }

    public void buildPatterns(String[] stopPatterns)
    {
        patterns = new Pattern[stopPatterns.length];
        matchers = new Matcher[stopPatterns.length];
        for (int i = 0; i < stopPatterns.length; ++i) {
            patterns[i] = Pattern.compile(stopPatterns[i]);
            matchers[i] = null;
        }
    }

    public boolean match(MeCabToken token)
    {
        String feature = token.getFeature();
        Matcher m = null;
        for (int i = 0; i < matchers.length; ++i) {
            m = matchers[i];
            if (m == null) {
                m = patterns[i].matcher(feature);
                matchers[i] = m;
            } else {
                m.reset(feature);
            }

            if (m.matches()) {
                return true;
            }
        }

        return false;
    }

    public Token next() throws IOException
    {
        Token token = input.next();

        while (token != null) {
            if (!(token instanceof MeCabToken)) {
                break;
            }
            if (!match((MeCabToken) token)) {
                break;
            }
            token = input.next();
        }

        return token;
    }
}
