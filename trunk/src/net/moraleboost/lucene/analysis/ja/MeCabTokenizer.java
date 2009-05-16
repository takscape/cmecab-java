/*
 **
 **  Mar. 5, 2008
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

import java.io.Reader;
import java.io.IOException;
import java.nio.CharBuffer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Token;

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.StandardTagger;

public class MeCabTokenizer extends Tokenizer
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    private int bufferSize = DEFAULT_BUFFER_SIZE;
    private int maxSize = DEFAULT_MAX_SIZE;

    private StringBuilder charSequence = null;
    private CharBuffer buffer = null;
    private Tagger tagger = null;
    private Node node = null;
    private int offset = 0;
    private boolean ownTagger = false;

    /**
     * MeCabを用いて入力を分かち書きするTokenizerを構築する。
     * 
     * @param in
     *            入力
     * @param dicCharset
     *            MeCabの辞書の文字コード
     * @param arg
     *            MeCabに与えるオプション
     * @throws IOException
     */
    public MeCabTokenizer(Reader in, String dicCharset, String arg)
    throws MeCabException, IOException
    {
        super(in);

        charSequence = new StringBuilder(bufferSize);
        buffer = CharBuffer.allocate(bufferSize);
        tagger = new StandardTagger(dicCharset, arg);
        ownTagger = true;

        parse();
    }

    /**
     * MeCabを用いて入力を分かち書きするTokenizerを構築する。
     * 
     * @param in
     *            入力
     * @param dicCharset
     *            MeCabの辞書の文字コード
     * @param arg
     *            MeCabに与えるオプション
     * @param bufferSize
     *            入力を吸い上げるための一時バッファの初期サイズ
     * @param maxSize
     *            入力から読み込んだデータの量がこの値を超えると、
     *            解析は失敗し、MeCabExceptionが発生する。
     * @throws IOException
     * @throws MeCabException
     */
    public MeCabTokenizer(Reader in, String dicCharset, String arg,
            int bufferSize, int maxSize)
    throws MeCabException, IOException
    {
        super(in);

        this.bufferSize = bufferSize;
        this.maxSize = maxSize;

        charSequence = new StringBuilder(bufferSize);
        buffer = CharBuffer.allocate(bufferSize);
        tagger = new StandardTagger(dicCharset, arg);

        parse();
    }
    
    public MeCabTokenizer(Reader in, Tagger tagger,
            int bufferSize, int maxSize)
    throws MeCabException, IOException
    {
        super(in);

        this.bufferSize = bufferSize;
        this.maxSize = maxSize;
        
        charSequence = new StringBuilder(bufferSize);
        buffer = CharBuffer.allocate(bufferSize);
        this.tagger = tagger;
        
        parse();
    }
    
    protected Tagger getTagger()
    {
        return tagger;
    }

    @Override
    public void close() throws IOException
    {
        if (ownTagger && tagger != null) {
            tagger.close();
        }
        node = null;

        super.close();
    }

    @Override
    public Token next() throws MeCabException, IOException
    {
        if (node == null || !node.hasNext()) {
            return null;
        }

        String tokenString = node.nextMorpheme();
        String blankString = node.blank();
        int start;
        int end;

        if (blankString != null) {
            start = offset + blankString.length();
            end = start + tokenString.length();
        } else {
            start = offset;
            end = start + tokenString.length();
        }

        offset = end;
        return new MeCabToken(tokenString, node.feature(), start, end);
    }

    private void parse() throws MeCabException, IOException
    {
        // drain input
        int nread = 0;
        charSequence.setLength(0);
        buffer.clear();
        while ((nread = input.read(buffer)) > 0) {
            buffer.rewind();
            charSequence.append(buffer, 0, nread);
            buffer.clear();
            if (charSequence.length() > maxSize) {
                throw new MeCabException("Max size exceeded.");
            }
        }

        // parse
        node = tagger.parse(charSequence);
    }
}
