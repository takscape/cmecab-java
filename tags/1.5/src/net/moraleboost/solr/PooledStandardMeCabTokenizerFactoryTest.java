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

import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Test;


public class PooledStandardMeCabTokenizerFactoryTest
{
    public static final String DIC_ENCODING = System
            .getProperty("net.moraleboost.mecab.encoding");

    @Test
    public void testCreate() throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("charset", DIC_ENCODING);
        args.put("arg", "");

        PooledStandardMeCabTokenizerFactory factory = new PooledStandardMeCabTokenizerFactory();
        factory.init(args);

        StringReader reader = new StringReader("本日は晴天なり。");
        TokenStream stream = factory.create(reader);

        Token token = new Token();
        if (stream.next(token) == null) {
            fail("No token.");
        }
    }
}
