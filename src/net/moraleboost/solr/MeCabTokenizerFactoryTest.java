package net.moraleboost.solr;

import static org.junit.Assert.fail;

import org.apache.lucene.analysis.TokenStream;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import java.io.StringReader;

public class MeCabTokenizerFactoryTest
{
    public static final String DIC_ENCODING = System
            .getProperty("net.moraleboost.mecab.encoding");

    @Test
    public void testCreate() throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("charset", DIC_ENCODING);
        args.put("arg", "");
        
        MeCabTokenizerFactory factory = new MeCabTokenizerFactory();
        factory.init(args);
        
        StringReader reader = new StringReader("本日は晴天なり。");
        TokenStream stream = factory.create(reader);

        if (stream.next() == null) {
            fail("No token.");
        }
    }
}
