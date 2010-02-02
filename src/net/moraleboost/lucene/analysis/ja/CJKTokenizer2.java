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

import net.moraleboost.io.BasicCodePointReader;
import net.moraleboost.io.CodePointReader;
import net.moraleboost.io.PushbackCodePointReader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.io.Reader;

/**
 * codepointを認識するCJKTokenizerクローン。<br>
 * <br>
 * CJKTokenizer2は、一種のステートマシンである。
 * {@link TokenInfo#type}変数がTokenizerの現在の状態を表し、
 * そこに入力される文字のタイプ{@link CharInfo#type}に
 * 応じて、次の状態に遷移する（あるいは、終了状態に遷移、
 * すなわち{@link CJKTokenizer2#next()}メソッドから抜ける）。<br>
 * <br>
 * {@link TokenInfo#type}に加え、Tokenizerの前の状態を記録しておくための
 * {@link TokenInfo#prevType}変数が存在する。
 * prevTypeは、CJK文字をスキャンする際に用いられる。
 *
 * 状態の一覧は、以下のとおり。<br>
 * <table>
 *   <tr>
 *      <th>typeの値</th>
 *      <th>説明</th>
 *   </tr>
 *   <tr>
 *      <td>null</td>
 *      <td>開始状態</td>
 *   </tr>
 *   <tr>
 *      <td>TOKENTYPE_SINGLE</td>
 *      <td>シングルバイト文字で構成されるトークンをスキャン中</td>
 *   </tr>
 *   <tr>
 *      <td>TOKENTYPE_DOUBLE</td>
 *      <td>CJK文字で構成されるトークンをスキャン中</td>
 *   </tr>
 *   <tr>
 *      <td>TOKENTYPE_NULL</td>
 *      <td>無視される文字（空白や記号）で構成されるトークンをスキャン中</td>
 *   </tr>
 * </table>
 * <br>
 * 文字のタイプの一覧は、以下のとおり。<br>
 * <table>
 *   <tr>
 *      <th>typeの値</th>
 *      <th>説明</th>
 *   </tr>
 *   <tr>
 *      <td>CHARTYPE_SINGLE</td>
 *      <td>シングルバイト文字</td>
 *   </tr>
 *   <tr>
 *      <td>CHARTYPE_DOUBLE</td>
 *      <td>CJK文字</td>
 *   </tr>
 *   <tr>
 *      <td>CHARTYPE_SYMBOL</td>
 *      <td>記号、空白</td>
 *   </tr>
 *   <tr>
 *      <td>CHARTYPE_EOS</td>
 *      <td>ストリームの終わり</td>
 *   </tr>
 * </table>
 * 
 * @author taketa
 * 
 */
public class CJKTokenizer2 extends Tokenizer
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
        /**
         * 処理したCharInfoを格納しておくbuffer
         */
        private CharInfo[] buffer = null;
        /**
         * buffer内に格納された総コードポイント数
         */
        private int readLength = 0;
        /**
         * buffer内に格納されたコードポイントのうち、
         * 実際にtokenを構成するのは、先頭からいくつ分なのか。
         */
        private int tokenLength = 0;
        /**
         * 現在スキャン中のtokenのタイプ
         */
        private String type = null;
        /**
         * 一つ前にスキャンしたtokenのタイプ
         */
        private String prevType = null;
        /**
         * スキャン終了フラグ
         */
        private boolean complete = false; // 終了フラグ
        /**
         * スキャン終了後に、
         * buffer末尾からいくつ分のコードポイントをストリームに戻すか。
         */
        private int pushbackSize = 0;
        /**
         * N-gramのN。1ならunigram、2ならbigam、3ならtrigram。
         */
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
        
        public String getPrevType()
        {
            return prevType;
        }

        public String getTokenString()
        {
            StringBuilder builder = new StringBuilder();
            for (int i=0; i<tokenLength; ++i) {
                builder.appendCodePoint(buffer[i].normCodePoint);
            }
            return builder.toString();
        }
        
        public int getStartOffset()
        {
            return (int)buffer[0].start;
        }
        
        public int getEndOffset()
        {
            return (int)buffer[tokenLength-1].end;
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
        
        private void changeTokenType(String tokenType)
        {
            if (type == null) {
                type = tokenType;
            } else {
                // 最後に読んだcharInfoを、バッファ先頭にコピー
                buffer[0] = buffer[readLength-1];
                readLength = 1;
                tokenLength = 0;
                prevType = type;
                type = tokenType;
            }
        }
        
        private void setComplete()
        {
            this.complete = true;
        }
        
        private void setPushbackSize(int pushbackSize)
        {
            this.pushbackSize = pushbackSize;
        }
        
        private void addToBuffer(CharInfo c)
        {
            buffer[readLength++] = c;
        }
        
        private void incrementTokenLength()
        {
            ++tokenLength;
        }
        
        private boolean isBufferFull()
        {
            return (readLength >= MAX_WORD_LEN);
        }
        
        private boolean isNgramComplete()
        {
            return (tokenLength >= ngram);
        }
        
        private int getReadLength()
        {
            return readLength;
        }
        
        private int getNgram()
        {
            return ngram;
        }

        public void handleChar(CharInfo c) throws IOException
        {
            // 文字をバッファに追加
            addToBuffer(c);

            // 現在のトークンタイプによって分岐
            if (type == null) {
                // 現在スキャン中のトークンなし。
                // 文字種によって、トークンタイプを決定。
                if (c.type == CHARTYPE_SINGLE) {
                    // singleトークンのスキャンに移行
                    changeTokenType(TOKENTYPE_SINGLE);
                    incrementTokenLength();
                } else if (c.type == CHARTYPE_DOUBLE) {
                    // doubleトークンのスキャンに移行
                    changeTokenType(TOKENTYPE_DOUBLE);
                    incrementTokenLength();
                    // unigramなら、直ちに終了状態に移行
                    if (getNgram() == 1) {
                        setComplete();
                    }
                } else if (c.type == CHARTYPE_SYMBOL) {
                    // nullトークンのスキャンに移行
                    changeTokenType(TOKENTYPE_NULL);
                    incrementTokenLength();
                } else {
                    // ストリームの終わり。
                    // 終了状態に移行
                    setComplete();
                }
            } else if (type == TOKENTYPE_SINGLE) {
                // 現在スキャン中のトークンはsingle。
                if (c.type == CHARTYPE_SINGLE) {
                    // ラテン文字
                    incrementTokenLength();
                    // バッファに空きがなくなった場合、一旦トークンとして切り出す。
                    if (isBufferFull()) {
                        setComplete();
                    }
                } else {
                    // CJK文字、記号・空白、EOSの場合は、
                    // 読んだ文字をストリームに戻し、
                    // 直前までのbuffer内容をtokenとして切り出す。
                    setComplete();
                    setPushbackSize(1);
                }
            } else if (type == TOKENTYPE_DOUBLE) {
                // 現在スキャン中のトークンはdouble。
                // ここに来た時点で、bufferには1文字分CJK文字が入っている。
                if (c.type == CHARTYPE_SINGLE) {
                    if (getPrevType() == TOKENTYPE_DOUBLE) {
                        // 前のトークンがdoubleであるなら、
                        // 現在のbuffer内容を捨て、そのまま次のsingleトークンの
                        // スキャンに移行。
                        changeTokenType(TOKENTYPE_SINGLE);
                        incrementTokenLength();
                    } else {
                        // 前のトークンがdoubleでないなら、
                        // ここでトークンを切り出す。
                        setComplete();
                        setPushbackSize(1);
                    }
                } else if (c.type == CHARTYPE_DOUBLE) {
                    // CJK文字
                    // buffer内の文字数がngram以上になった場合は、
                    // 現在のbufferをtokenとして切り出すと共に、
                    // 1文字残してbuffer内容をpushback。
                    incrementTokenLength();
                    if (isNgramComplete()) {
                        setComplete();
                        setPushbackSize(getReadLength()-1);
                    }
                } else if (c.type == CHARTYPE_SYMBOL) {
                    // 記号
                    if (prevType == TOKENTYPE_DOUBLE) {
                        // 前のトークンがDOUBLEであるなら、
                        // 現在のbuffer内容を捨て、次のNULLトークンのスキャンに移行。
                        changeTokenType(TOKENTYPE_NULL);
                        incrementTokenLength();
                    } else {
                        // 前のトークンがdoubleでないなら、
                        // ここでトークンを切り出す。
                        setComplete();
                        setPushbackSize(1);
                    }
                } else {
                    // EOS
                    if (prevType == TOKENTYPE_DOUBLE) {
                        changeTokenType(null);
                        setComplete();
                    } else {
                        setComplete();
                        setPushbackSize(1);
                    }
                }
            } else if (type == TOKENTYPE_NULL) {
                // 現在スキャン中のトークンはnull。
                if (c.type == CHARTYPE_SYMBOL) {
                    // 記号・空白
                    incrementTokenLength();
                    // バッファに空きがなくなった場合、一旦トークンとして切り出す。
                    if (isBufferFull()) {
                        setComplete();
                    }
                } else {
                    // ラテン文字、CJK文字、EOSの場合、
                    // 直前までのバッファ内容をトークンとして切り出す。
                    setComplete();
                    setPushbackSize(1);
                }
            } else {
                throw new IOException("Illegal state.");
            }
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
    }

    /**
     * コードポイントを読み出すreader
     */
    private PushbackCodePointReader pbinput = null;
    /**
     * 現在解析中のトークンの情報
     */
    private TokenInfo tokenInfo = null;
    /**
     * トークンのターム属性
     */
    private TermAttribute termAttribute = null;
    /**
     * トークンのオフセット属性
     */
    private OffsetAttribute offsetAttribute = null;
    /**
     * トークンのタイプ属性
     */
    private TypeAttribute typeAttribute = null;
    
    public CJKTokenizer2(Reader in)
    {
        this(in, DEFAULT_NGRAM);
    }

    public CJKTokenizer2(Reader in, int ngram)
    {
        super(in);
        init(in, ngram);
    }
    
    public CJKTokenizer2(AttributeSource source, Reader in)
    {
        this(source, in, DEFAULT_NGRAM);
    }
    
    public CJKTokenizer2(AttributeSource source, Reader in, int ngram)
    {
        super(source, in);
        init(in, ngram);
    }
    
    public CJKTokenizer2(AttributeFactory factory, Reader in)
    {
        this(factory, in, DEFAULT_NGRAM);
    }
    
    public CJKTokenizer2(AttributeFactory factory, Reader in, int ngram)
    {
        super(factory, in);
        init(in, ngram);
    }
    
    private void init(Reader in, int ngram)
    {
        pbinput = new PushbackCodePointReader(new BasicCodePointReader(in), ngram);
        assert(ngram > 0);
        tokenInfo = new TokenInfo(ngram);
        termAttribute = (TermAttribute)addAttribute(TermAttribute.class);
        offsetAttribute = (OffsetAttribute)addAttribute(OffsetAttribute.class);
        typeAttribute = (TypeAttribute)addAttribute(TypeAttribute.class);
    }

    public boolean incrementToken() throws IOException
    {
        clearAttributes();
        
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
            
            // System.out.println(tokenInfo.toToken());
        } while (tokenInfo.getType() == TOKENTYPE_NULL);
        
        String type = tokenInfo.getType();
        if (type == null) {
            return false;
        } else {
            String term = tokenInfo.getTokenString();
            termAttribute.setTermBuffer(term);
            termAttribute.setTermLength(term.length());
            offsetAttribute.setOffset(
                    correctOffset(tokenInfo.getStartOffset()),
                    correctOffset(tokenInfo.getEndOffset()));
            typeAttribute.setType(type);
            return true;
        }
    }
    
    @Override
    public void end()
    {
        int offset = correctOffset((int)pbinput.getPosition());
        offsetAttribute.setOffset(offset, offset);
    }
    
    @Override
    public void reset(Reader in) throws IOException
    {
        super.reset(in);
        pbinput = new PushbackCodePointReader(new BasicCodePointReader(in), pbinput.getStackSize());
        // 二度呼び出してprevTypeをnullにする。
        tokenInfo.clear(); tokenInfo.clear();
    }
}
