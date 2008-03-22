package net.moraleboost.solr;

import static org.junit.Assert.fail;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import java.io.StringReader;


public class FeatureRegexFilterFactoryTest
{
    public static final String DIC_ENCODING = System
            .getProperty("net.moraleboost.mecab.encoding");

    TokenStream tokenizer = null;
    
    @Before
    public void setUp()
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("charset", DIC_ENCODING);
        args.put("arg", "");
        
        MeCabTokenizerFactory factory = new MeCabTokenizerFactory();
        factory.init(args);
        
        StringReader reader = new StringReader("本日は晴天なり。");
        tokenizer = factory.create(reader);
    }
    
    @Test
    public void testCreate() throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put("charset", "Shift_JIS");
        args.put("source", "test" + java.io.File.separator + "regexfilter.txt");
        
        FeatureRegexFilterFactory factory = new FeatureRegexFilterFactory();
        factory.init(args);
        
        TokenStream stream = factory.create(tokenizer);
        Token token;
        while ((token = stream.next()) != null) {
            String termText = token.termText();
            if (termText.equals("は") || termText.equals("なり")) {
                fail("Filter not working.");
            }
        }
    }
}
