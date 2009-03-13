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
    public static final int MAX_WORD_LEN = 255;

    public static final String TOKENTYPE_NULL = "null".intern();
    public static final String TOKENTYPE_SINGLE = "single".intern();
    public static final String TOKENTYPE_DOUBLE = "double".intern();

    public static final int CHARTYPE_EOS    = 0; // End Of Stream
    public static final int CHARTYPE_SYMBOL = 1; // 記号
    public static final int CHARTYPE_SINGLE = 2; // "シングルバイト"文字。単語区切り。
    public static final int CHARTYPE_DOUBLE = 3; // "ダブルバイト"文字。N-gram。
    
    public static final int DEFAULT_NGRAM = 2;
    
    public static class CharInfo
    {
        public int codePoint = -1; // original code point
        public int normCodePoint = -1; // normalized code point
        public long start = 0; // start offset in chars
        public long end = 0; // end offset in chars
        public int type = CHARTYPE_SYMBOL; // one of CHARTPE_*

        /**
         * 半角濁点ならtrue
         * @return
         */
        public boolean isHalfwidthKatakanaVoicedSoundMark()
        {
            return (codePoint == 0xFF9E);
        }
        
        /**
         * 半角半濁点ならtrue
         * @return
         */
        public boolean isHalfwidthKatakanaVoicedSemiSoundMark()
        {
            return (codePoint == 0xFF9F);
        }
        
        /**
         * 半角の「カ・サ・タ・ハ行」の文字、もしくは「ウ」ならtrue
         * @return
         */
        public boolean isCandidateForVoicedSound()
        {
            return ((0xFF76 <= codePoint && codePoint <= 0xFF84) || // カ～ト
                    (0xFF8A <= codePoint && codePoint <= 0xFF8E) || // ハ～ホ
                    codePoint == 0xFF73);                           // ウ
        }
        
        /**
         * 半角の「ハ行」の文字ならtrue
         * @return
         */
        public boolean isCandidateForSemiVoicedSound()
        {
            return (0xFF8A <= codePoint && codePoint <= 0xFF8E); // ハ～ホ
        }

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
                type = CHARTYPE_EOS;
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
        private CharInfo[] buffer = null;
        private int readLength = 0; // in code points
        private int tokenLength = 0; // in code points
        private String type = null;
        private String prevType = null; // 直前にスキャンしたトークンのタイプ

        private boolean complete = false;
        private int pushbackSize = 0;
        private int ngram = 0;

        public TokenInfo(int ngram)
        {
            this.buffer = new CharInfo[MAX_WORD_LEN];
            this.ngram = ngram;
            
            clear();
        }
        
        public String getType()
        {
            return type;
        }

        public boolean isComplete()
        {
            return complete;
        }

        public boolean shouldPushback()
        {
            return (pushbackSize > 0);
        }

        public void clear()
        {
            readLength = 0;
            tokenLength = 0;
            prevType = type;
            type = null;
            complete = false;
            pushbackSize = 0;
        }
        
        private void setTokenType(String tokenType)
        {
            type = tokenType;
        }
        
        private void changeTokenType(String tokenType)
        {
            prevType = type;
            type = tokenType;
        }
        
        private void setComplete(int pushbackSize)
        {
            this.complete = true;
            this.pushbackSize = pushbackSize;
        }
        
        private void addToBuffer(CharInfo c, boolean isPartOfToken)
        {
            buffer[readLength++] = c;
            if (isPartOfToken) {
                ++tokenLength;
            }
        }

        public void handleChar(CharInfo c) throws IOException
        {
            // 現在のトークンタイプによって分岐
            boolean isPartOfToken = true;
            if (type == null) {
                // 現在スキャン中のトークンなし。
                // 文字種によって、トークンタイプを決定。
                if (c.type == CHARTYPE_SINGLE) {
                    setTokenType(TOKENTYPE_SINGLE);
                } else if (c.type == CHARTYPE_DOUBLE) {
                    setTokenType(TOKENTYPE_DOUBLE);
                    if (ngram == 1) {
                        setComplete(0);
                    }
                } else if (c.type == CHARTYPE_SYMBOL) {
                    setTokenType(TOKENTYPE_NULL);
                } else {
                    // EOS
                    isPartOfToken = false;
                    setComplete(0);
                }
            } else if (type == TOKENTYPE_SINGLE) {
                // 現在スキャン中のトークンは単語境界区切り
                if (c.type == CHARTYPE_SINGLE) {
                    // ラテン文字
                    // バッファに空きがなくなった場合、一旦トークンとして切り出す。
                    if (readLength >= MAX_WORD_LEN) {
                        setComplete(0);
                    }
                } else if (c.type == CHARTYPE_DOUBLE) {
                    // CJK文字
                    // 直前までのbuffer内容をtokenとして切り出すと共に、
                    // 1文字pushback。
                    isPartOfToken = false;
                    setComplete(1);
                } else if (c.type == CHARTYPE_SYMBOL) {
                    // 記号
                    // 直前までのbuffer内容をtokenとして切り出す。
                    isPartOfToken = false;
                    setComplete(1);
                } else {
                    // EOS
                    // 直前までのbuffer内容をtokenとして切り出す。
                    isPartOfToken = false;
                    setComplete(1);
                }
            } else if (type == TOKENTYPE_DOUBLE) {
                // 現在スキャン中のトークンはN-gram
                // ここに来た時点で、bufferには1文字分CJK文字が入っている。
                if (c.type == CHARTYPE_SINGLE) {
                    // ラテン文字
                    if (prevType == TOKENTYPE_DOUBLE) {
                        // 前のトークンがDOUBLEであるなら、
                        // 現在のbuffer内容を捨て、そのまま次のSINGLEトークンの
                        // スキャンに移行。
                        changeTokenType(TOKENTYPE_SINGLE);
                    } else {
                        // 前のトークンがDOUBLEでないなら、
                        // ここでトークンを切り出す。
                        isPartOfToken = false;
                        setComplete(1);
                    }
                } else if (c.type == CHARTYPE_DOUBLE) {
                    // CJK文字
                    // buffer内の文字数がngram以上になった場合は、
                    // 現在のbufferをtokenとして切り出すと共に、
                    // 1文字残してbuffer内容をpushback。
                    if (tokenLength+1 >= ngram) {
                        setComplete(readLength);
                    }
                } else if (c.type == CHARTYPE_SYMBOL) {
                    // 記号
                    if (prevType == TOKENTYPE_DOUBLE) {
                        // 前のトークンがDOUBLEであるなら、
                        // 現在のbuffer内容を捨て、次のNULLトークンのスキャンに移行。
                        changeTokenType(TOKENTYPE_NULL);
                    } else {
                        // 直前までのbufferをtokenとして切り出す。
                        isPartOfToken = false;
                        setComplete(1);
                    }
                } else {
                    // EOS
                    isPartOfToken = false;
                    setComplete(1);
                }
            } else if (type == TOKENTYPE_NULL) {
                if (c.type == CHARTYPE_SINGLE) {
                    // ラテン文字
                    // 直前までのバッファ内容をトークンとして切り出す。
                    isPartOfToken = false;
                    setComplete(1);
                }else if (c.type == CHARTYPE_DOUBLE) {
                    // CJK文字
                    // 直前までのバッファ内容をトークンとして切り出す。
                    isPartOfToken = false;
                    setComplete(1);
                } else if (c.type == CHARTYPE_SYMBOL) {
                    // 記号
                    // 継続してスキャン。
                    if (readLength >= MAX_WORD_LEN) {
                        setComplete(0);
                    }
                } else {
                    // EOS
                    // 直前までのバッファ内容をトークンとして切り出す。
                    isPartOfToken = false;
                    setComplete(1);
                }
            } else {
                throw new IOException("Illegal state.");
            }
            
            addToBuffer(c, isPartOfToken);
        }
        
        public void pushback(PushbackCodePointReader reader)
        throws IOException
        {
            CharInfo charInfo;
            for (int i=readLength-1; i>=(readLength-pushbackSize); --i) {
                charInfo = buffer[i];
                reader.unread(charInfo.codePoint,
                        (int)(charInfo.end - charInfo.start));
            }
        }

        public Token toToken()
        {
            if (type == null) {
                return null;
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i=0; i<tokenLength; ++i) {
                    builder.appendCodePoint(buffer[i].normCodePoint);
                }
                return new Token(
                        builder.toString(),
                        (int)buffer[0].start,
                        (int)buffer[tokenLength-1].end,
                        type);
            }
        }
    }

    /**
     * コードポイントを読み出すreader
     */
    private PushbackCodePointReader pbinput = null;
    /**
     * 現在解析中のトークンの情報
     */
    private TokenInfo tokenInfo = null;
    
    public CJKTokenizer2(Reader in)
    {
        this(in, DEFAULT_NGRAM);
    }

    public CJKTokenizer2(Reader in, int ngram)
    {
        super(in);
        pbinput = new PushbackCodePointReader(new BasicCodePointReader(in), ngram);
        assert(ngram > 0);
        tokenInfo = new TokenInfo(ngram);
    }

    public final Token next() throws IOException
    {
        do {
            tokenInfo.clear();
            CharInfo charInfo;
            do {
                charInfo = new CharInfo();
                charInfo.read(pbinput);
                tokenInfo.handleChar(charInfo);
            } while (!tokenInfo.isComplete());
            
            if (tokenInfo.shouldPushback()) {
                tokenInfo.pushback(pbinput);
            }
        } while (tokenInfo.getType() == TOKENTYPE_NULL);

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
