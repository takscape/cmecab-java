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

    /**
     * featureが指定したパターンに合致するtokenをふるい落とすフィルタを構築する。
     * 
     * @param input
     *            上流TokenStream
     * @param stopPatterns
     *            Java正規表現の配列を指定。
     *            featureがこのパターンのいずれかにマッチするtokenはフィルタリングされる。
     */
    public FeatureRegexFilter(TokenStream input, String[] stopPatterns)
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
     * tokenのfeatureが構築時に指定したパターンのいずれかにマッチするかどうかを調べる。
     * 
     * @param token
     *            トークン
     * @return いずれかのパターンにマッチすればtrue。全くマッチしなければfalse。
     */
    private boolean match(Token token)
    {
        String feature = token.type();
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

    public Token next(Token reusableToken) throws IOException
    {
        Token token = input.next(reusableToken);

        while (token != null) {
            if (!match(token)) {
                break;
            }
            token = input.next(reusableToken);
        }

        return token;
    }
}
