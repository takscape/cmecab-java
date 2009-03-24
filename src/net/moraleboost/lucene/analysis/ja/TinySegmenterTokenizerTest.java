package net.moraleboost.lucene.analysis.ja;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.apache.lucene.analysis.Token;
import org.junit.Test;

public class TinySegmenterTokenizerTest
{
    @Test
    public void testNext()
    throws Exception
    {
        String str = "本日は晴天なり。";
        StringReader reader = new StringReader(str);
        
        TinySegmenterTokenizer tokenizer = new TinySegmenterTokenizer(reader);
        
        String[] tokens = {
                "本日",
                "は",
                "晴天",
                "なり",
                "。"
        };
        
        int[][] positions = {
                {0, 2},
                {2, 3},
                {3, 5},
                {5, 7},
                {7, 8}
        };

        Token token;
        int i = 0;
        while ((token = tokenizer.next()) != null) {
            assertEquals(tokens[i], token.term());
            assertEquals(positions[i][0], token.startOffset());
            assertEquals(positions[i][1], token.endOffset());
            ++i;
        }
        assertEquals(tokens.length, i);
    }
}
