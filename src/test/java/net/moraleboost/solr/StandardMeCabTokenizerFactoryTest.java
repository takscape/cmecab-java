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
import org.junit.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class StandardMeCabTokenizerFactoryTest
{
    @Test
    public void testCreate() throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("arg", "");

        StandardMeCabTokenizerFactory factory = new StandardMeCabTokenizerFactory(args);

        StringReader reader = new StringReader("本日は晴天なり。");
        TokenStream stream = factory.create(reader);

        if (!stream.incrementToken()) {
            fail("No token.");
        }
    }
}
