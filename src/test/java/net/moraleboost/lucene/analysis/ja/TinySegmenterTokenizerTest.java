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
package net.moraleboost.lucene.analysis.ja;

import org.junit.Test;

import java.io.StringReader;

import static net.moraleboost.lucene.analysis.ja.Util.compareTokens;

public class TinySegmenterTokenizerTest
{
    @Test
    public void testNext()
    throws Exception
    {
        String str = "本日は晴天なり。";
        StringReader reader = new StringReader(str);
        
        TinySegmenterTokenizer tokenizer = new TinySegmenterTokenizer(reader);
        
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
        
        compareTokens(tokenizer, terms, offsets);
    }
}
