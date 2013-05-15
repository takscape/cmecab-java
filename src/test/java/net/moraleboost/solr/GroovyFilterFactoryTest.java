package net.moraleboost.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import net.moraleboost.lucene.analysis.ja.StandardMeCabTokenizer;
import net.moraleboost.mecab.impl.StandardTagger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class GroovyFilterFactoryTest
{
    @Test
    public void test() throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("file", "test/groovy/TestGroovyFilter.groovy");

        GroovyFilterFactory factory = new GroovyFilterFactory(args);

        Reader reader = new StringReader("本日は晴天なり。");

        Tokenizer tkn = new StandardMeCabTokenizer(reader, new StandardTagger(""), Integer.MAX_VALUE);
        TokenStream flt = factory.create(tkn);

        CharTermAttribute attr = flt.getAttribute(CharTermAttribute.class);
        while (flt.incrementToken()) {
            assertEquals("replaced", attr.toString());
        }
    }
}
