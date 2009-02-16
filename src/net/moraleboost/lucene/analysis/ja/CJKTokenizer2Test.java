package net.moraleboost.lucene.analysis.ja;

import static org.junit.Assert.*;
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
                    "丁",
                    "乱丁",
                    "丁",
                    "は",
                    "abcd",
                    "def",
                    "お取",
                    "取替",
                    "替え",
                    "えし",
                    "しま",
                    "ます",
                    "す"
            };
            
            int [][] offsets = {
            		{0,9},   // this_book
            		{10,11}, // s
            		{11,13}, // 落丁
            		{12,13}, // 丁
            		{14,16}, // 乱丁
            		{15,16}, // 丁
            		{18,19}, // は
            		{19,23}, // abcd
            		{24,27}, // def
            		{27,29}, // お取
            		{28,30}, // 取替
            		{29,31}, // 替え
            		{30,32}, // えし
            		{31,33}, // しま
            		{32,34}, // ます
            		{33,34}  // す
            };
            
            int i=0;
            while ((token=tokenizer.next()) != null) {
            	assertEquals(tokens[i], token.termText());
            	assertEquals("Wrong start offset", offsets[i][0], token.startOffset());
            	assertEquals("Wrong end offset", offsets[i][1], token.endOffset());
                ++i;
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
