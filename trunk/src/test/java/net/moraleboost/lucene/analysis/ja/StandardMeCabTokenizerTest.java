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

import static net.moraleboost.lucene.analysis.ja.CJKTokenizer2Test.compareTokens;

import java.io.StringReader;

import net.moraleboost.mecab.impl.StandardTagger;
import org.junit.Test;

public class StandardMeCabTokenizerTest
{
    @Test
    public void testSurrogatePair() throws Exception
    {
        // 好物は「ほっけ」です。
        String str = "好物は\uD867\uDE3Dです。";
        StringReader reader = new StringReader(str);
        StandardMeCabTokenizer tokenizer =
                new StandardMeCabTokenizer(reader, new StandardTagger(""), Integer.MAX_VALUE);

        String[] terms = {
                "好物",
                "は",
                "\uD867\uDE3D",
                "です",
                "。"
        };

        int[][] offsets = {
                { 0, 2 }, // 好物
                { 2, 3 }, // は
                { 3, 5 }, // ほっけ
                { 5, 7 }, // です
                { 7, 8 }  // 。
        };
        
        compareTokens(tokenizer, terms, offsets);
    }

    @Test
    public void testWhiteSpaces() throws Exception
    {
        String str = "本日   は晴天\nなり\r\n。";
        StringReader reader = new StringReader(str);
        StandardMeCabTokenizer tokenizer =
                new StandardMeCabTokenizer(reader, new StandardTagger(""), Integer.MAX_VALUE);

        String[] terms = {
                "本日",
                "は",
                "晴天",
                "なり",
                "\r",
                "。"
        };

        int[][] offsets = {
                { 0, 2 }, // 本日
                // ここに空白のオフセット3charが入る
                { 5, 6 }, // は
                { 6, 8 }, // 晴天
                // ここにLFのオフセット1charが入る
                { 9, 11 }, // なり
                { 11, 12 }, // CR
                // ここにLFのオフセット1charが入る
                { 13, 14 } // 。
        };
        
        compareTokens(tokenizer, terms, offsets);
    }
}
