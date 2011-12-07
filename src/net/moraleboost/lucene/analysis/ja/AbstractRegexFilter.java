package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractRegexFilter extends TokenFilter
{
    private Pattern[] patterns;
    private Matcher[] matchers;

    public AbstractRegexFilter(TokenStream input, String[] stopPatterns)
    {
        super(input);
        buildPatterns(stopPatterns);
    }

    private void buildPatterns(String[] stopPatterns)
    {
        patterns = new Pattern[stopPatterns.length];
        matchers = new Matcher[stopPatterns.length];

        for (int i = 0; i < stopPatterns.length; ++i) {
            patterns[i] = Pattern.compile(stopPatterns[i]);
            matchers[i] = null;
        }
    }

    /**
     * valueが構築時に指定したパターンのいずれかにマッチするかどうかを調べる。
     *
     * @param value 文字列
     * @return いずれかのパターンにマッチすればtrue。全くマッチしなければfalse。
     */
    protected boolean match(CharSequence value)
    {
        Matcher m;

        for (int i = 0; i < matchers.length; ++i) {
            m = matchers[i];
            if (m == null) {
                m = patterns[i].matcher(value);
                matchers[i] = m;
            } else {
                m.reset(value);
            }

            if (m.matches()) {
                return true;
            }
        }

        return false;
    }
}
