/*
**
**  May. 13, 2008
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

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.io.PushbackReader;

public final class CJKTokenizer2 extends Tokenizer
{
    private static final int MAX_WORD_LEN = 255;

    private static final String TOKENTYPE_SINGLE = "single".intern();
    private static final String TOKENTYPE_DOUBLE = "double".intern();

    private static final int CHARTYPE_SYMBOL = 0;
    private static final int CHARTYPE_SINGLE = 1;
    private static final int CHARTYPE_DOUBLE = 2;

    private int offset = 0;
    private final char[] buffer = new char[MAX_WORD_LEN];
    private int lastCharType = CHARTYPE_SYMBOL;
    private PushbackReader pbinput = null;

    public CJKTokenizer2(Reader in)
    {
        pbinput = new PushbackReader(in, 1);
        input = pbinput;
    }

    public final Token next() throws IOException
    {
        int length = 0;
        int start = offset;
        int corg = -1;
        int c = -1;
        int prevCharType = lastCharType;
        int charType = lastCharType;
        String tokenType = null;
        Character.UnicodeBlock ub = null;

        while (true) {
            corg = pbinput.read();
            c = corg;

            // 文字種の調査
            prevCharType = charType;
            if (c >= 0) {
                ++offset;
                ub = Character.UnicodeBlock.of(c);
                if (ub == Character.UnicodeBlock.BASIC_LATIN || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                    // ラテン文字及び記号。単語境界で分割する。
                    if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                        // 全角アルファベットを半角に変換する。
                        int i = (int) c;
                        i = i - 65248;
                        c = (char) i;
                    }
                    
                    if (Character.isLetterOrDigit(c) || (c == '_') || (c == '+') || (c == '#')) {
                        charType = CHARTYPE_SINGLE;
                        c = Character.toLowerCase(c);
                    } else {
                        charType = CHARTYPE_SYMBOL;
                    }
                } else {
                    if (Character.isLetter(c)) {
                        charType = CHARTYPE_DOUBLE;
                    } else {
                        charType = CHARTYPE_SYMBOL;
                    }
                }
            } else {
                // end of input.
                charType = CHARTYPE_SYMBOL;
            }

            // 現在のトークンタイプによって分岐
            if (tokenType == null) {
                // 現在スキャン中のトークンなし。
                if (c < 0) {
                    // end of input
                    return null;
                }
                
                // 文字種によって、トークンタイプを決定
                if (charType == CHARTYPE_SINGLE) {
                    buffer[0] = (char)c;
                    start = offset-1;
                    length = 1;
                    tokenType = TOKENTYPE_SINGLE;
                } else if (charType == CHARTYPE_DOUBLE) {
                    buffer[0] = (char)c;
                    start = offset-1;
                    length = 1;
                    tokenType = TOKENTYPE_DOUBLE;
                } else {
                    // 記号は読み飛ばす
                }
            } else if (tokenType == TOKENTYPE_SINGLE) {
                // 現在スキャン中のトークンは単語境界区切り
                if (charType == CHARTYPE_SINGLE) {
                    buffer[length++] = (char)c;
                    if (length >= MAX_WORD_LEN) {
                        // バッファに空きがないので、ここで一旦トークンとして切り出す。
                        break;
                    }
                } else if (charType == CHARTYPE_DOUBLE) {
                    pbinput.unread(c);
                    --offset;
                    charType = prevCharType;
                    break;
                } else {
                    break;
                }
            } else if (tokenType == TOKENTYPE_DOUBLE) {
                // 現在スキャン中のトークンはbi-gram
                if (charType == CHARTYPE_DOUBLE) {
                    buffer[length++] = (char)c;
                    pbinput.unread(c);
                    --offset;
                    charType = prevCharType;
                    break;
                } else if (charType == CHARTYPE_SINGLE) {
                    pbinput.unread(c);
                    --offset;
                    charType = prevCharType;
                }
                
                // 現在のバッファの内容は1文字しかない。
                // 前の文字がDOUBLEである場合は、このまま新しいトークンのスキャンに移行。
                if (lastCharType == CHARTYPE_DOUBLE) {
                    length = 0;
                    tokenType = null;
                    lastCharType = charType;
                } else {
                    break;
                }
            } else {
                throw new IOException("Illegal state.");
            }
        }

        lastCharType = charType;
        return new Token(new String(buffer, 0, length), start, start
                + length, tokenType);
    }
    
    public static void main(String[] args) throws Exception
    {
        java.io.StringReader in = new java.io.StringReader(args[0]);
        CJKTokenizer2 tokenizer = new CJKTokenizer2(in);
        Token token;
        
        while ((token=tokenizer.next()) != null) {
            System.out.println(Integer.toString(token.startOffset()) + "-" + Integer.toString(token.endOffset()) + ": " + token.termText());
        }
    }
}
