/*
 **
 **  Mar. 24, 2009
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
package net.moraleboost.tinysegmenter;

import static org.junit.Assert.*;

import java.io.StringReader;

import net.moraleboost.io.BasicCodePointReader;

import org.junit.Test;

public class TinySegmenterTest
{
    @Test
    public void testTokenize()
    throws Exception
    {
        String str = "本日は晴天なり。";
        StringReader reader = new StringReader(str);
        BasicCodePointReader cpreader = new BasicCodePointReader(reader);
        
        TinySegmenter segmenter = new TinySegmenter(cpreader);
        
        String[] terms = {
                "本日",
                "は",
                "晴天",
                "なり",
                "。"
        };
        
        int[][] offsets = {
                {0, 2},
                {2, 3},
                {3, 5},
                {5, 7},
                {7, 8}
        };
        
        TinySegmenter.Token token = null;
        int i = 0;
        while ((token = segmenter.next()) != null) {
            assertEquals(terms[i], token.str);
            assertEquals(offsets[i][0], token.start);
            assertEquals(offsets[i][1], token.end);
            ++i;
        }
        
        assertEquals(terms.length, i);
    }
    
    @Test
    public void testMinimalBufferSize()
    throws Exception
    {
        String str =
            "メロスは激怒した。" +
            "必ず、かの邪智暴虐の王を除かなければならぬと決意した。" +
            "メロスには政治がわからぬ。" +
            "メロスは、村の牧人である。" +
            "笛を吹き、羊と遊んで暮して来た。" +
            "けれども邪悪に対しては、人一倍に敏感であった。";
        StringReader reader = new StringReader(str);
        StringReader reader2 = new StringReader(str);
        BasicCodePointReader cpreader = new BasicCodePointReader(reader);
        BasicCodePointReader cpreader2 = new BasicCodePointReader(reader2);
        
        TinySegmenter segmenter =
            new TinySegmenter(cpreader, 7, TinySegmenter.DEFAULT_MAX_TOKEN_SIZE);
        TinySegmenter segmenter2 =
            new TinySegmenter(cpreader2, 1024, TinySegmenter.DEFAULT_MAX_TOKEN_SIZE);
        
        TinySegmenter.Token token = null;
        TinySegmenter.Token token2 = null;
        while (true) {
            token = segmenter.next();
            token2 = segmenter2.next();
            
            assertEquals(token2, token);
            
            if (token == null || token2 == null) {
                break;
            }
        }
        
        assertNull(token);
        assertNull(token2);
    }
    
    @Test
    public void testEmptyStream()
    throws Exception
    {
        String str = "";
        StringReader reader = new StringReader(str);
        BasicCodePointReader cpreader = new BasicCodePointReader(reader);
        
        TinySegmenter segmenter = new TinySegmenter(cpreader);
        
        assertNull(segmenter.next());
    }
    
    @Test
    public void testMaxTokenSize()
    throws Exception
    {
        String str = "一日作さざれば、一日食わず。";
        StringReader reader = new StringReader(str);
        BasicCodePointReader cpreader = new BasicCodePointReader(reader);
        
        TinySegmenter segmenter = new TinySegmenter(cpreader, 1024, 2);

        String[] terms = {
                "一日",
                "作",
                "さざ",
                "れ",
                "ば",
                "、",
                "一",
                "日",
                "食わ",
                "ず",
                "。"
        };
        
        int[][] offsets = {
                {0, 2},
                {2, 3},
                {3, 5},
                {5, 6},
                {6, 7},
                {7, 8},
                {8, 9},
                {9, 10},
                {10, 12},
                {12, 13},
                {13, 14}
        };

        TinySegmenter.Token token = null;
        int i = 0;
        while ((token = segmenter.next()) != null) {
            assertEquals(terms[i], token.str);
            assertEquals(offsets[i][0], token.start);
            assertEquals(offsets[i][1], token.end);
            ++i;
        }
        
        assertEquals(terms.length, i);
    }
}
