package net.moraleboost.lucene.analysis.ja;

import net.moraleboost.mecab.impl.StandardTagger;
import org.junit.Test;

import java.io.StringReader;

import static net.moraleboost.lucene.analysis.ja.CJKTokenizer2Test.compareTokens;

public class JapaneseNormalizationFilterTest
{
    @Test
    public void testBlank() throws Exception
    {
        StringReader reader = new StringReader("あ　い　う え\nお\tか");
        StandardMeCabTokenizer tokenizer =
                new StandardMeCabTokenizer(reader, new StandardTagger(""), Integer.MAX_VALUE);
        JapaneseNormalizationFilter filter = new JapaneseNormalizationFilter(tokenizer, false, false, true);

        // 全角スペースは半角になっている。半角スペース、改行、タブは形態素解析器がスキップ。
        String[] terms = {
                "あ", " ", "い", " ", "う", "え", "お", "か"
        };

        int[][] offsets = {
                {0,1}, {1,2}, {2,3}, {3,4}, {4,5}, {6,7}, {8,9}, {10,11}
        };

        compareTokens(filter, terms, offsets);
    }

    @Test
    public void testAlphaDigits() throws Exception
    {
        StringReader reader = new StringReader("Ｖｉｅｗカードを５１枚作った～。");
        StandardMeCabTokenizer tokenizer =
                new StandardMeCabTokenizer(reader, new StandardTagger(""), Integer.MAX_VALUE);
        JapaneseNormalizationFilter filter = new JapaneseNormalizationFilter(tokenizer, true, false, false);

        // 英数字は半角に統一。「～」はそのまま。
        String[] terms = {
                "View",
                "カード","を",
                "5","1","枚",
                "作っ","た","～","。"
        };

        int[][] offsets = {
                {0,4},
                {4,7}, {7,8},
                {8,9}, {9,10}, {10,11},
                {11,13}, {13,14}, {14,15}, {15,16}
        };

        compareTokens(filter, terms, offsets);
    }

    @Test
    public void testKana() throws Exception
    {
        StringReader reader = new StringReader("あのｶﾝｶﾞﾙｰはいいﾊﾟﾝﾁを持っている。");
        StandardMeCabTokenizer tokenizer =
                new StandardMeCabTokenizer(reader, new StandardTagger(""), Integer.MAX_VALUE);
        JapaneseNormalizationFilter filter = new JapaneseNormalizationFilter(tokenizer, false, true, false);

        // 濁点、半濁点が前の文字に結合されていることを確認。
        String[] terms = {
                "あの",
                "カンガルー",
                "は",
                "いい",
                "パンチ",
                "を",
                "持っ",
                "て",
                "いる",
                "。"
        };

        int[][] offsets = {
                { 0, 2 },   // あの
                { 2, 8 },   // カンガルー
                { 8, 9 },   // は
                { 9, 11 },  // いい
                { 11, 15 }, // パンチ
                { 15, 16 }, // を
                { 16, 18 }, // 持っ
                { 18, 19 }, // て
                { 19, 21 }, // いる
                { 21, 22 }  // 。
        };

        compareTokens(filter, terms, offsets);
    }
}
