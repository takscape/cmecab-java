package net.moraleboost.lucene.analysis.ja;

import static org.junit.Assert.fail;

import java.io.StringReader;

import org.apache.lucene.analysis.Token;

import org.junit.Test;

public class CJKTokenizer2Test
{
    @Test
    public void testTokenize()
    {
        try {
            String str = "this_ＢＯＯＫ’s落丁、乱丁  はaBCd.defお取替えします。";
            StringReader reader = new StringReader(str);
            CJKTokenizer2 tokenizer = new CJKTokenizer2(reader);
            Token token;
            
            String[] tokens = {
                    "this_book",
                    "s",
                    "落丁",
                    "乱丁",
                    "は",
                    "abcd",
                    "def",
                    "お取",
                    "取替",
                    "替え",
                    "えし",
                    "しま",
                    "ます"
            };
            
            int i=0;
            while ((token=tokenizer.next()) != null) {
                if (!token.termText().equals(tokens[i])) {
                    fail("Wrong token: " + token.termText());
                }
                ++i;
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
