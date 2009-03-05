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

import net.moraleboost.mecab.util.BasicCodePointReader;
import net.moraleboost.mecab.util.CodePointReader;
import net.moraleboost.mecab.util.PushbackCodePointReader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * codepointを認識するCJKTokenizerクローン。
 * 
 * @author taketa
 * 
 */
public final class CJKTokenizer2 extends Tokenizer
{
    private static final int MAX_WORD_LEN = 255;

    private static final String TOKENTYPE_SINGLE = "single".intern();
    private static final String TOKENTYPE_DOUBLE = "double".intern();

    private static final int CHARTYPE_SYMBOL = 0;
    private static final int CHARTYPE_SINGLE = 1;
    private static final int CHARTYPE_DOUBLE = 2;

    public static class CharInfo
    {
        public int codePoint = -1; // original code point
        public int normCodePoint = -1; // normalized code point
        public long start = 0; // start offset in chars
        public long end = 0; // end offset in chars
        public int type = CHARTYPE_SYMBOL; // one of CHARTPE_*

        public void read(CodePointReader reader) throws IOException
        {
            Character.UnicodeBlock ub = null;

            start = reader.getPosition();
            codePoint = reader.read();
            normCodePoint = codePoint;
            end = reader.getPosition();
            type = CHARTYPE_SYMBOL;

            // 文字種別を判別
            if (codePoint < 0) {
                // end of stream
            } else {
                ub = Character.UnicodeBlock.of(codePoint);
                if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                    // ラテン文字及び記号。
                    if (Character.isLetterOrDigit(normCodePoint)
                            || (normCodePoint == '_') || (normCodePoint == '+')
                            || (normCodePoint == '#')) {
                        type = CHARTYPE_SINGLE;
                        // ラテンアルファベットを小文字に正規化
                        normCodePoint = Character.toLowerCase(normCodePoint);
                    }
                } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                    // 全角数字、アルファベット、記号、および半角かな、ハングル等
                    if ((0xFF10 <= codePoint && codePoint <= 0xFF19) || // 全角数字
                        (0xFF21 <= codePoint && codePoint <= 0xFF3A) || // 全角アルファベット大文字
                        (0xFF41 <= codePoint && codePoint <= 0xFF5A))   // 全角アルファベット小文字
                    {
                        // 全角アルファベットおよび数字を半角小文字に変換
                        normCodePoint = Character.toLowerCase(codePoint - 65248);
                        type = CHARTYPE_SINGLE;
                    } else if (Character.isLetter(codePoint)) {
                        type = CHARTYPE_DOUBLE;
                    }
                } else {
                    if (Character.isLetter(codePoint)) {
                        type = CHARTYPE_DOUBLE;
                    } else {
                        type = CHARTYPE_SYMBOL;
                    }
                }
            }
        }
    }

    public static class TokenInfo
    {
        private int[] buffer;
        private int start; // start offset in chars
        private int end; // end offset in chars
        private int length; // token length in code points
        private String type;

        private boolean complete;
        private boolean pushback;

        public TokenInfo()
        {
            buffer = new int[MAX_WORD_LEN];
            clear();
        }

        public boolean isComplete()
        {
            return complete;
        }

        public boolean shouldPushback()
        {
            return pushback;
        }

        public void clear()
        {
            start = 0;
            end = 0;
            length = 0;
            type = null;
            complete = false;
            pushback = false;
        }

        public void handleChar(CharInfo c) throws IOException
        {
            // 現在のトークンタイプによって分岐
            if (type == null) {
                // 現在スキャン中のトークンなし。
                if (c.codePoint < 0) {
                    // end of input
                    complete = true;
                }

                // 文字種によって、トークンタイプを決定
                if (c.type == CHARTYPE_SINGLE) {
                    buffer[0] = c.normCodePoint;
                    start = (int)c.start;
                    end = (int)c.end;
                    length = 1;
                    type = TOKENTYPE_SINGLE;
                } else if (c.type == CHARTYPE_DOUBLE) {
                    buffer[0] = c.normCodePoint;
                    start = (int)c.start;
                    end = (int)c.end;
                    length = 1;
                    type = TOKENTYPE_DOUBLE;
                } else {
                    // 記号は読み飛ばす
                }
            } else if (type == TOKENTYPE_SINGLE) {
                // 現在スキャン中のトークンは単語境界区切り
                if (c.type == CHARTYPE_SINGLE) {
                    // ラテン文字
                    // bufferに付け加える
                    buffer[length++] = c.normCodePoint;
                    end = (int)c.end;

                    // バッファに空きがなくなった場合、一旦トークンとして切り出す。
                    if (length >= MAX_WORD_LEN) {
                        complete = true;
                    }
                } else if (c.type == CHARTYPE_DOUBLE) {
                    // CJK文字
                    // 1文字pushbackして、現在のbufferをtokenとして切り出す。
                    complete = true;
                    pushback = true;
                } else {
                    // 記号
                    // 現在のbufferをtokenとして切り出す。
                    complete = true;
                }
            } else if (type == TOKENTYPE_DOUBLE) {
                // 現在スキャン中のトークンはbi-gram
                // ここに来た時点で、bufferには1文字分CJK文字が入っている。
                if (c.type == CHARTYPE_DOUBLE) {
                    // CJK文字
                    // bufferに付け加えると共に、1文字pushbackして、
                    // 現在のbufferをtokenとして切り出す。
                    buffer[length++] = c.normCodePoint;
                    end = (int)c.end;
                    complete = true;
                    pushback = true;
                } else if (c.type == CHARTYPE_SINGLE) {
                    // ラテン文字
                    // 1文字pushbackし、現在のbufferをtokenとして切り出す。
                    complete = true;
                    pushback = true;
                } else {
                    // 記号
                    // 現在のbufferをtokenとして切り出す。
                    complete = true;
                }
            } else {
                throw new IOException("Illegal state.");
            }
        }

        public Token toToken()
        {
            if (type == null) {
                return null;
            } else {
                return new Token(new String(buffer, 0, length), start, end,
                        type);
            }
        }
    }

    /**
     * コードポイントを読み出すreader
     */
    private PushbackCodePointReader pbinput = null;
    /**
     * 新しく読んだ文字の情報
     */
    private CharInfo charInfo = new CharInfo();
    /**
     * 現在解析中のトークンの情報
     */
    private TokenInfo tokenInfo = new TokenInfo();

    public CJKTokenizer2(Reader in)
    {
        super(in);
        pbinput = new PushbackCodePointReader(new BasicCodePointReader(in), 1);
    }

    public final Token next() throws IOException
    {
        tokenInfo.clear();

        do {
            charInfo.read(pbinput);
            tokenInfo.handleChar(charInfo);
        } while (!tokenInfo.isComplete());

        if (tokenInfo.shouldPushback()) {
            pbinput.unread(charInfo.codePoint,
                    (int)(charInfo.end - charInfo.start));
        }

        return tokenInfo.toToken();
    }

    public static void main(String[] args) throws Exception
    {
        java.io.StringReader in = new java.io.StringReader(args[0]);
        CJKTokenizer2 tokenizer = new CJKTokenizer2(in);
        Token token;

        while ((token = tokenizer.next()) != null) {
            System.out.println(Integer.toString(token.startOffset()) + "-"
                    + Integer.toString(token.endOffset()) + ": "
                    + token.termText());
        }
    }
}
