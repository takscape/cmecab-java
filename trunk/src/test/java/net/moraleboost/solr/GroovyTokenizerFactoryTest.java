package net.moraleboost.solr;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GroovyTokenizerFactoryTest
{
    @Test
    public void test() throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("file", "src/test/groovy/TestGroovyTokenizer.groovy");
        args.put("classpath", "src\\test\\groovy");
        args.put("encoding", "UTF-8");

        GroovyTokenizerFactory factory = new GroovyTokenizerFactory(args);

        Reader reader = new StringReader("");

        Tokenizer tkn = factory.create(reader);

        CharTermAttribute attr = tkn.getAttribute(CharTermAttribute.class);

        assertTrue(tkn.incrementToken());
        assertEquals("本日", attr.toString());
        assertTrue(tkn.incrementToken());
        assertEquals("は", attr.toString());
        assertTrue(tkn.incrementToken());
        assertEquals("晴天", attr.toString());
        assertTrue(tkn.incrementToken());
        assertEquals("なり", attr.toString());
        assertTrue(tkn.incrementToken());
        assertEquals("。", attr.toString());
        assertFalse(tkn.incrementToken());
    }
}
