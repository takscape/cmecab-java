/*
 * Based on TinySegmenter 0.1 -- Super compact Japanese tokenizer in Javascript
 * (c) 2008 Taku Kudo <taku@chasen.org>
 * TinySegmenter is freely distributable under the terms of a new BSD licence.
 * For details, see http://chasen.org/~taku/software/TinySegmenter/LICENCE.txt
 * 
 * Ported to Java by Kohei TAKETA <k-tak@void.in>
 */
package net.moraleboost.mecab.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TinySegmenter
{
    private static final Set<Integer> CHINESE_NUMBER_SET;
    static {
        Set<Integer> s = new HashSet<Integer>();
        s.addAll(getCodePoints("一二三四五六七八九十百千万億兆"));
        CHINESE_NUMBER_SET = Collections.unmodifiableSet(s);
    }
    
    private char getCharType(int cp)
    {
        if (CHINESE_NUMBER_SET.contains(cp)) {
            // [一二三四五六七八九十百千万億兆]
            return 'M';
        } else if (
                (0x4E00 <= cp && cp <= 0x9fa0) ||
                cp == '々' || cp == '〆' || cp == 'ヵ' || cp == 'ヶ') {
            // [一-龠々〆ヵヶ]
            return 'H';
        } else if (0x3041 <= cp && cp <= 0x3093) {
            // [ぁ-ん]
            return 'I';
        } else if (
                (0x30a1 <= cp && cp <= 0x30f4) || cp == 0x30fc ||
                (0xff71 <= cp && cp <= 0xff9e) || cp == 0xff70) {
            // [ァ-ヴーｱ-ﾝﾞｰ]
            return 'K';
        } else if (
                ('a' <= cp && cp <= 'z') || ('A' <= cp && cp <= 'Z') ||
                ('ａ' <= cp && cp <= 'ｚ') || ('Ａ' <= cp && cp <= 'Ｚ')) {
            // [a-zA-Zａ-ｚＡ-Ｚ]
            return 'A';
        } else if (
                ('0' <= cp && cp <= '9') || ('０' <= cp && cp <= '９')) {
            // [0-9０-９]
            return 'N';
        } else {
            return 'O';
        }
    }

    private static List<Integer> getCodePoints(String str)
    {
        int count = str.codePointCount(0, str.length());
        List<Integer> result = new ArrayList<Integer>(count);
        int charIndex = 0;
        while (charIndex < str.length()) {
            int cp = str.codePointAt(charIndex);
            result.add(cp);
            charIndex += Character.charCount(cp);
        }

        return result;
    }
}
