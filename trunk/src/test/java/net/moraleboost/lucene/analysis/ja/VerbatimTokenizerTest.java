package net.moraleboost.lucene.analysis.ja;

import org.junit.Test;

import java.io.StringReader;

import static net.moraleboost.lucene.analysis.ja.CJKTokenizer2Test.compareTokens;

public class VerbatimTokenizerTest
{
    @Test
    public void test()
    throws Exception
    {
        String str = "本日は晴天なり。";
        StringReader reader = new StringReader(str);
        
        VerbatimTokenizer tokenizer = new VerbatimTokenizer(reader);
        
        String[] terms = {
                "本日は晴天なり。"
        };
        
        int [][] offsets = {
                {0, 8}
        };
        
        compareTokens(tokenizer, terms, offsets);
    }
    
    @Test
    public void testBufferSize()
    throws Exception
    {
        String str = "本日は晴天なり。";
        StringReader reader = new StringReader(str);
        
        VerbatimTokenizer tokenizer = new VerbatimTokenizer(reader, 1);
        
        String[] terms = {
                "本日は晴天なり。"
        };
        
        int [][] offsets = {
                {0, 8}
        };
        
        compareTokens(tokenizer, terms, offsets);
    }
}
