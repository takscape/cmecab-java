/*
 * Based on TinySegmenter 0.1 -- Super compact Japanese tokenizer in Javascript
 * (c) 2008 Taku Kudo <taku@chasen.org>
 * TinySegmenter is freely distributable under the terms of a new BSD licence.
 * For details, see http://chasen.org/~taku/software/TinySegmenter/LICENCE.txt
 * 
 * Ported to Java by Kohei TAKETA <k-tak@void.in>
 */
package net.moraleboost.tinysegmenter;

import static net.moraleboost.tinysegmenter.TinySegmenterConstants.*;

import net.moraleboost.mecab.util.CodePointReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TinySegmenter
{
    private static final Set<Integer> CHINESE_NUMBER_SET;
    
    static {
        Set<Integer> s = new HashSet<Integer>();
        s.addAll(getCodePoints("一二三四五六七八九十百千万億兆"));
        CHINESE_NUMBER_SET = Collections.unmodifiableSet(s);
        
        Map<String, Integer> m;
    }
    
    private static String getCharType(int cp)
    {
        if (CHINESE_NUMBER_SET.contains(cp)) {
            // [一二三四五六七八九十百千万億兆]
            return "M";
        } else if (
                (0x4E00 <= cp && cp <= 0x9fa0) ||
                cp == '々' || cp == '〆' || cp == 'ヵ' || cp == 'ヶ') {
            // [一-龠々〆ヵヶ]
            return "H";
        } else if (0x3041 <= cp && cp <= 0x3093) {
            // [ぁ-ん]
            return "I";
        } else if (
                (0x30a1 <= cp && cp <= 0x30f4) || cp == 0x30fc ||
                (0xff71 <= cp && cp <= 0xff9e) || cp == 0xff70) {
            // [ァ-ヴーｱ-ﾝﾞｰ]
            return "K";
        } else if (
                ('a' <= cp && cp <= 'z') || ('A' <= cp && cp <= 'Z') ||
                ('ａ' <= cp && cp <= 'ｚ') || ('Ａ' <= cp && cp <= 'Ｚ')) {
            // [a-zA-Zａ-ｚＡ-Ｚ]
            return "A";
        } else if (
                ('0' <= cp && cp <= '9') || ('０' <= cp && cp <= '９')) {
            // [0-9０-９]
            return "N";
        } else {
            return "O";
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
    
    public static class CharInfo
    {
        public static final char[] EMPTY_CHARS = new char[0];

        public int cp;
        public char[] chars;
        public String ctype;
        public long start;
        public long end;
        
        public CharInfo()
        {
        }
        
        public void read(CodePointReader reader) throws IOException
        {
            start = reader.getPosition();
            cp = reader.read();
            end = reader.getPosition();
            if (cp >= 0) {
                chars = Character.toChars(cp);
                ctype = getCharType(cp);
            } else {
                chars = EMPTY_CHARS;
                ctype = "O";
            }
        }
    }
    
    private CodePointReader reader;
    private ArrayList<CharInfo> buffer = new ArrayList<CharInfo>(4096);
    private int start = 0;
    private int end = 0;
    
    public TinySegmenter(CodePointReader reader)
    {
        this.reader = reader;
        initBuffer();
    }
    
    private void initBuffer()
    {
        CharInfo c;
        
        for (int i=0; i<3; ++i) {
            c = new CharInfo();
            c.cp = 0;
            c.chars = CharInfo.EMPTY_CHARS;
            c.ctype = "O";
            c.start = -1;
            c.end = -1;
            buffer.add(c);
        }
        start = 3;
        end = 3;
    }
    
    private void fillBuffer()
    throws IOException
    {
        CharInfo c;
        
        c = new CharInfo();
        c.read(reader);
        if (c.cp < 0) {
            // EOS
            for (int i=0; i<3; ++i) {
                c = new CharInfo();
                c.cp = 0;
                c.chars = CharInfo.EMPTY_CHARS;
                c.ctype = "O";
                c.start = -1;
                c.end = -1;
                buffer.add(c);
            }
        }
    }
    
    public List<String> segment()
    throws IOException
    {
        List<String> ret = new LinkedList<String>();
        
        
        return ret;
    }
}
