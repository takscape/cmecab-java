package net.moraleboost.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CSVUtilTest
{
    @Test
    public void testEscape()
    {
        for (int i=0; i<65536; ++i) {
            String str = Character.toString((char)i);
            if (str.equals("\"")) {
                assertEquals("\"\"\"\"", CSVUtil.escape(str));
            } else if (str.equals(" ")) {
                assertEquals("\" \"", CSVUtil.escape(str));
            } else if (str.equals("\t")) {
                assertEquals("\"\t\"", CSVUtil.escape(str));
            } else if (str.equals(",")) {
                assertEquals("\",\"", CSVUtil.escape(str));
            } else {
                assertEquals(str, CSVUtil.escape(str));
            }
        }

        assertEquals("abc", CSVUtil.escape("abc"));
        assertEquals("あいうえお", CSVUtil.escape("あいうえお"));
        assertEquals("\" This \"\"is a pen., what?\t\"", CSVUtil.escape(" This \"is a pen., what?\t"));
    }

    @Test
    public void testTokenize()
    {
        // 空文字列は、サイズ0の配列になる
        String str = "";
        String[] answer = new String[] {};
        String[] tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // 「""」は、空文字列一つからなる配列になる
        str = "\"\"";
        answer = new String[] {""};
        tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // ","は、空文字列2つからなる配列になる
        str = ",";
        answer = new String[] {"", ""};
        tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // 非クォート文字列（1列）
        str = "abc";
        answer = new String[] { "abc" };
        tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // クォート文字列（1列）
        str = " \t\"ab\"\"c\" ";
        answer = new String[] {"ab\"c"};
        tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // 非クォート文字列（複数列）
        str = "a,b ,  c";
        answer = new String[] {"a", "b ", "c"};
        tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // クォート文字列（複数列）
        str = "\"a\"  , \" bc\"\"d \", \"efg";
        answer = new String[] {"a", " bc\"d ", "efg"};
        tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // 混在
        str = "a, \"bcd\" , efg  ";
        answer = new String[] {"a", "bcd", "efg  "};
        tokens = CSVUtil.tokenize(str, 100);
        assertArrayEquals(answer, tokens);

        // max指定
        str = "a ,   ";
        answer = new String[] {"a ,   "};
        tokens = CSVUtil.tokenize(str, 1);
        assertArrayEquals(answer, tokens);

        str = "a ,";
        answer = new String[] {"a ,"};
        tokens = CSVUtil.tokenize(str, 1);
        assertArrayEquals(answer, tokens);

        str = "a , \"bcd\"efg";
        answer = new String[] {"a , \"bcd\"efg"};
        tokens = CSVUtil.tokenize(str, 1);
        assertArrayEquals(answer, tokens);

        str = "a , bcd, \tefg,def";
        answer = new String[] {"a ", "bcd, \tefg,def"};
        tokens = CSVUtil.tokenize(str, 2);
        assertArrayEquals(answer, tokens);
    }
}
