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

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class FeatureRegexFilterFactoryTest
{
    TokenStream tokenizer = null;

    @Before
    public void setUp()
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("arg", "");

        StandardMeCabTokenizerFactory factory = new StandardMeCabTokenizerFactory(args);

        StringReader reader = new StringReader("本日は晴天なり。");
        tokenizer = factory.create(reader);
    }

    @Test
    public void testCreate() throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("charset", "Shift_JIS");
        args.put("source", "test" + java.io.File.separator + "regexfilter.txt");

        FeatureRegexFilterFactory factory = new FeatureRegexFilterFactory(args);

        TokenStream stream = factory.create(tokenizer);
        CharTermAttribute termAttr =
            stream.getAttribute(CharTermAttribute.class);
        while (stream.incrementToken()) {
            String termText = new String(termAttr.buffer(), 0, termAttr.length());
            //System.out.println("token: " + termText);
            if (termText.equals("は") || termText.equals("なり")) {
                fail("Filter not working.");
            }
        }
    }
}
