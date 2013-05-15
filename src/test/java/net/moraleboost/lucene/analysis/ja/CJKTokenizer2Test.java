/*
 **
 **  Feb. 17, 2009
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
package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CJKTokenizer2Test
{
    public static void compareTokens(TokenStream ts, String[] terms, int[][] offsets)
    throws IOException
    {
        int i = 0;
        
        CharTermAttribute termAttr =
            ts.getAttribute(CharTermAttribute.class);
        OffsetAttribute offAttr =
            ts.getAttribute(OffsetAttribute.class);
        
        while (ts.incrementToken()) {
            String term = new String(termAttr.buffer(), 0, termAttr.length());
            int startOff = offAttr.startOffset();
            int endOff = offAttr.endOffset();
            
            //System.out.println(term);
            assertEquals(terms[i], term);
            assertEquals("Wrong start offset", offsets[i][0], startOff);
            assertEquals("Wrong end offset", offsets[i][1], endOff);
            ++i;
        }
        
        assertEquals(terms.length, i);
    }

    @Test
    public void testTokenizeSurrogate() throws Exception
    {
        // 好物は「ほっけ」です。
        String str = "好物は\uD867\uDE3Dです。";
        StringReader reader = new StringReader(str);
        CJKTokenizer2 tokenizer = new CJKTokenizer2(reader);

        String[] terms = {
                "好物",
                "物は",
                "は\uD867\uDE3D",
                "\uD867\uDE3Dで",
                "です"
        };

        int[][] offsets = {
                { 0, 2 },
                { 1, 3 },
                { 2, 5 },
                { 3, 6 },
                { 5, 7 }
        };
        
        compareTokens(tokenizer, terms, offsets);
    }
    
    @Test
    public void testUnigram()
    throws Exception
    {
        String str = "あいうえお";
        StringReader reader = new StringReader(str);
        CJKTokenizer2 tokenizer = new CJKTokenizer2(reader, 1);
        
        String[] terms = {
                "あ",
                "い",
                "う",
                "え",
                "お"
        };
        
        int[][] offsets = {
                { 0, 1 },   // あ
                { 1, 2 },   // い
                { 2, 3 },   // う
                { 3, 4 },   // え
                { 4, 5 }    // お
        };

        compareTokens(tokenizer, terms, offsets);
    }
    
    @Test
    public void testTrigram()
    throws Exception
    {
        String str = "ルート選択があるプロセス[1]件目";
        StringReader reader = new StringReader(str);
        CJKTokenizer2 tokenizer = new CJKTokenizer2(reader, 3);
        
        String[] terms = {
                "ルート",
                "ート選",
                "ト選択",
                "選択が",
                "択があ",
                "がある",
                "あるプ",
                "るプロ",
                "プロセ",
                "ロセス",
                "1",
                "件目"
        };

        int[][] offsets = {
                { 0, 3 },   // ルート
                { 1, 4 },   // ート選
                { 2, 5 },   // ト選択
                { 3, 6 },   // 選択が
                { 4, 7 },   // 択があ
                { 5, 8 },   // がある
                { 6, 9 },   // あるプ
                { 7, 10 },  // るプロ
                { 8, 11 },  // プロセ
                { 9, 12 },  // ロセス
                { 13, 14 }, // 1
                { 15, 17 }  // 件目
        };

        compareTokens(tokenizer, terms, offsets);
    }
    
    @Test
    public void testTokenizeHalfwidthKatakana()
    {
        try {
            String str = "ﾎﾝｼﾞﾂﾊ､ﾊﾝﾍﾟﾝｦｼｮｸｼﾀ｡";
            StringReader reader = new StringReader(str);
            CJKTokenizer2 tokenizer = new CJKTokenizer2(reader);
            
            String[] terms = {
                    "ﾎﾝ",
                    "ﾝｼ",
                    "ｼﾞ",
                    "ﾞﾂ",
                    "ﾂﾊ",
                    "ﾊﾝ",
                    "ﾝﾍ",
                    "ﾍﾟ",
                    "ﾟﾝ",
                    "ﾝｦ",
                    "ｦｼ",
                    "ｼｮ",
                    "ｮｸ",
                    "ｸｼ",
                    "ｼﾀ"
            };

            int[][] offsets = {
                    { 0, 2 },   // ﾎﾝ
                    { 1, 3 },   // ﾝｼ
                    { 2, 4 },   // ｼﾞ
                    { 3, 5 },   // ﾞﾂ
                    { 4, 6 },   // ﾂﾊ
                    { 7, 9 },   // ﾊﾝ
                    { 8, 10 },  // ﾝﾍ
                    { 9, 11 },  // ﾍﾟ
                    { 10, 12 }, // ﾟﾝ
                    { 11, 13 }, // ﾝｦ
                    { 12, 14 }, // ｦｼ
                    { 13, 15 }, // ｼｮ
                    { 14, 16 }, // ｮｸ
                    { 15, 17 }, // ｸｼ
                    { 16, 18 }, // ｼﾀ
            };
            
            compareTokens(tokenizer, terms, offsets);
            
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testTokenize()
    {
        try {
            String str = "this_ＢＯＯＫ’s落丁、乱丁  はaBCd.defお取替えします";
            StringReader reader = new StringReader(str);
            CJKTokenizer2 tokenizer = new CJKTokenizer2(reader);

            String[] terms = {
                    "this_book", // ここでsingle->single, single->null
                    // ここでnull->single
                    "s", // ここでsingle->double
                    "落丁", // ここでdouble->null
                    // ここでnull->double
                    "乱丁", // ここでdouble->null
                    // ここでnull->null, null->double
                    "は", // ここでdouble->single
                    "abcd", // ここでsingle->null
                    // ここでnull->single
                    "def", // ここでsingle->double
                    "お取", // ここでdouble->double
                    "取替",
                    "替え",
                    "えし",
                    "しま",
                    "ます"
            };

            int[][] offsets = {
                    { 0, 9 }, // this_book
                    { 10, 11 }, // s
                    { 11, 13 }, // 落丁
                    { 14, 16 }, // 乱丁
                    { 18, 19 }, // は
                    { 19, 23 }, // abcd
                    { 24, 27 }, // def
                    { 27, 29 }, // お取
                    { 28, 30 }, // 取替
                    { 29, 31 }, // 替え
                    { 30, 32 }, // えし
                    { 31, 33 }, // しま
                    { 32, 34 }  // ます
            };

            compareTokens(tokenizer, terms, offsets);

        } catch (Exception e) {
            fail(e.toString());
        }
    }
}