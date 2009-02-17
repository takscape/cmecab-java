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
package net.moraleboost.mecab.util;

import static org.junit.Assert.*;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class BasicCodePointReaderTest
{
    @Test
    public void testBasic()
    throws IOException
    {
        String str = "あaいbうcえdお";
        int[] answer = getCodePoints(str);
        long[] positions = getPositions(str);
        CodePointReader reader = new BasicCodePointReader(new StringReader(str));
        if (!match(reader, answer, positions)) {
            fail("コードポイントが一致しません。");
        }
    }

    @Test
    public void testSurrogatePair()
    throws IOException
    {
        int scp = 0x00010400;
        int[] answer = new int[] {
                scp, 'あ', 'a', 'い', scp, scp, 'd', 'お', scp
        };
        long[] positions = new long[] {
        		0, 2, 3, 4, 5, 7, 9, 10, 11, 13
        };
        
        String str = new String(answer, 0, answer.length);
        CodePointReader reader = new BasicCodePointReader(new StringReader(str));
        if (!match(reader, answer, positions)) {
            fail("コードポイントが一致しません。");
        }
    }
    
    @Test
    public void testEndWithHighSurrogate()
    throws IOException
    {
        int scp = 0x00010400;
        String base = "本日は晴天なり";
        char highSurrogate = Character.toChars(scp)[0];
        assertTrue(Character.isHighSurrogate(highSurrogate));
        
        CharArrayWriter writer = new CharArrayWriter();
        writer.write(base);
        writer.write(highSurrogate);
        
        int[] answer = getCodePoints(
        		base + (char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        long[] positions = getPositions(
        		base + (char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        CodePointReader reader =
        	new BasicCodePointReader(new CharArrayReader(writer.toCharArray()));
        if (!match(reader, answer, positions)) {
        	fail("コードポイントが一致しません。");
        }
    }
    
    @Test
    public void testEndWithLowSurrogate()
    throws IOException
    {
        int scp = 0x00010400;
        String base = "本日は晴天なり";
        char lowSurrogate = Character.toChars(scp)[1];
        assertTrue(Character.isLowSurrogate(lowSurrogate));
        
        CharArrayWriter writer = new CharArrayWriter();
        writer.write(base);
        writer.write(lowSurrogate);
        
        int[] answer = getCodePoints(
        		base + (char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        long[] positions = getPositions(
        		base + (char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        CodePointReader reader =
        	new BasicCodePointReader(new CharArrayReader(writer.toCharArray()));
        if (!match(reader, answer, positions)) {
        	fail("コードポイントが一致しません。");
        }
    }
    
    @Test
    public void testStartWithHighSurrogate()
    throws IOException
    {
        int scp = 0x00010400;
        String base = "本日は晴天なり";
        char highSurrogate = Character.toChars(scp)[0];
        assertTrue(Character.isHighSurrogate(highSurrogate));
        
        CharArrayWriter writer = new CharArrayWriter();
        writer.write(highSurrogate);
        writer.write(base);
        
        int[] answer = getCodePoints(
        		(char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT + base);
        long[] positions = getPositions(
        		(char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT + base);
        CodePointReader reader =
        	new BasicCodePointReader(new CharArrayReader(writer.toCharArray()));
        if (!match(reader, answer, positions)) {
        	fail("コードポイントが一致しません。");
        }
    }
    
    @Test
    public void testStartWithLowSurrogate()
    throws IOException
    {
        int scp = 0x00010400;
        String base = "本日は晴天なり";
        char lowSurrogate = Character.toChars(scp)[1];
        assertTrue(Character.isLowSurrogate(lowSurrogate));
        
        CharArrayWriter writer = new CharArrayWriter();
        writer.write(lowSurrogate); // low surrogate
        writer.write(base);
        
        int[] answer = getCodePoints(
        		(char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT + base);
        long[] positions = getPositions(
        		(char)BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT + base);
        CodePointReader reader =
        	new BasicCodePointReader(new CharArrayReader(writer.toCharArray()));
        if (!match(reader, answer, positions)) {
        	fail("コードポイントが一致しません。");
        }
    }

    @Test
    public void testIllformedSurrogate()
    throws IOException
    {
        int scp = 0x00010400;
        int[] original = new int[] {
                'あ', 'a', 'い', scp, scp, 'd', 'お'
        };
        // high surrogateを破壊した場合の正解
        int[] answer1 = new int[] {
                'あ', 'a', 'い',
                'a', BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT,
                scp, 'd', 'お'
        };
        long[] positions1 = new long[] {
        		0,
        		1, 2, 3,
        		4, 5,
        		7, 8, 9
        };
        // low surrogateを破壊した場合の正解
        int[] answer2 = new int[] {
                'あ', 'a', 'い',
                BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT, 'a',
                scp, 'd', 'お'
        };
        long[] positions2 = new long[] {
        		0,
        		1, 2, 3,
        		4, 5,
        		7, 8, 9
        };
        
        // 不正なデータを作成
        char[] chars1 =
            new String(original, 0, original.length).toCharArray();
        char[] chars2 =
            new String(original, 0, original.length).toCharArray();

        // high surrogateを破壊
        chars1[3] = 'a';
        String ill1 = new String(chars1);
        
        // low surrogateを破壊
        chars2[4] = 'a';
        String ill2 = new String(chars2);

        if (!match(new BasicCodePointReader(new StringReader(ill1)), answer1, positions1)) {
            fail("Low surrogateが単独で存在する場合のコードポイントが一致しません。");
        }
        if (!match(new BasicCodePointReader(new StringReader(ill2)), answer2, positions2)) {
            fail("High surrogateが単独で存在する場合のコードポイントが一致しません。");
        }
    }

    private boolean match(CodePointReader reader, int[] answer, long[] positions)
    throws IOException
    {
        int i=0;
        int cp;
        while ((cp = reader.read()) >= 0) {
            if (cp != answer[i]) {
                return false;
            }
            if(reader.getPosition() != positions[i+1]) {
            	return false;
            }
            ++i;
        }
        
        return (i == answer.length);
    }

    private int[] getCodePoints(String str)
    {
        int count = str.codePointCount(0, str.length());
        int[] result = new int[count];
        int cpIndex=0, charIndex=0;
        while (charIndex < str.length()) {
            int cp = str.codePointAt(charIndex);
            result[cpIndex++] = cp;
            charIndex += Character.charCount(cp);
        }
        
        return result;
    }
    
    private long[] getPositions(String str)
    {
    	int count = str.codePointCount(0, str.length());
    	long[] positions = new long[count+1];
    	int cpIndex = 0, charIndex = 0;
    	while (charIndex < str.length()) {
    		int cp = str.codePointAt(charIndex);
    		positions[cpIndex++] = charIndex;
    		charIndex += Character.charCount(cp);
    	}
    	positions[cpIndex] = charIndex;
    	
    	return positions;
    }
}
