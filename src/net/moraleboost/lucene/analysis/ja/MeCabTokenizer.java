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
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.Node;

public class MeCabTokenizer extends Tokenizer
{
    public static final int DEFAULT_BUFFER_INITIAL_SIZE = 4096;
    public static final int DEFAULT_BUFFER_SHRINK_THRESHOLD = 5 * 1024 * 1024;
    public static final int DEFAULT_BUFFER_SHRINK_TARGET = 1024 * 1024;
    public static final int DEFAULT_BUFFER_MAX_SIZE = 10 * 1024 * 1024;

    private int bufferInitialSize = DEFAULT_BUFFER_INITIAL_SIZE;
    private int bufferShrinkThreshold = DEFAULT_BUFFER_SHRINK_THRESHOLD;
    private int bufferShrinkTarget = DEFAULT_BUFFER_SHRINK_TARGET;
    private int bufferMaxSize = DEFAULT_BUFFER_MAX_SIZE;

    private StringBuilder buffer = null;
    private CharBuffer tmpBuffer = null;
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

        buffer = new StringBuilder(bufferInitialSize);
        tmpBuffer = CharBuffer.allocate(bufferInitialSize);
        tagger = new Tagger(dicCharset, arg);
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
     * @param initialSize
     *            入力を吸い上げるための一時バッファの初期サイズ
     * @param shrinkThreshold
     *            一時バッファのサイズがこの値を超えると、shrinkTargetまで縮小される。
     * @param shrinkTarget
     *            一時バッファのサイズがshrinkThresholdを超えると、このサイズまで縮小される。
     * @param maxSize
     *            一時バッファのサイズがこの値を超えると、解析は失敗し、MeCabExceptionが発生する。
     * @throws IOException
     */
    public MeCabTokenizer(Reader in, String dicCharset, String arg,
            int initialSize, int shrinkThreshold, int shrinkTarget, int maxSize)
    throws MeCabException, IOException
    {
        super(in);

        bufferInitialSize = initialSize;
        bufferShrinkThreshold = shrinkThreshold;
        bufferShrinkTarget = shrinkTarget;
        bufferMaxSize = maxSize;

        buffer = new StringBuilder(bufferInitialSize);
        tmpBuffer = CharBuffer.allocate(bufferInitialSize);
        tagger = new Tagger(dicCharset, arg);

        parse();
    }
    
    protected MeCabTokenizer(Reader in, Tagger tagger,
            int initialSize, int shrinkThreshold, int shrinkTarget, int maxSize)
    throws MeCabException, IOException
    {
        super(in);

        bufferInitialSize = initialSize;
        bufferShrinkThreshold = shrinkThreshold;
        bufferShrinkTarget = shrinkTarget;
        bufferMaxSize = maxSize;
        
        buffer = new StringBuilder(bufferInitialSize);
        tmpBuffer = CharBuffer.allocate(bufferInitialSize);
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
        String rsurface = node.rsurface();
        int end = offset + rsurface.length();
        int start = end - tokenString.length();
        offset = end;

        return new MeCabToken(tokenString, node.feature(), start, end);
    }

    private void parse() throws MeCabException, IOException
    {
        // drain input
        int nread = 0;
        buffer.setLength(0);
        tmpBuffer.clear();
        while ((nread = input.read(tmpBuffer)) > 0) {
            tmpBuffer.rewind();
            buffer.append(tmpBuffer, 0, nread);
            tmpBuffer.clear();
            if (buffer.length() > bufferMaxSize) {
                throw new MeCabException("Buffer overflow");
            }
        }

        // parse
        node = tagger.parse(buffer);

        // shrink buffer if exceeded BUFFER_SHRINK_THRESOLD
        if (buffer.length() > bufferShrinkThreshold) {
            buffer = new StringBuilder(bufferShrinkTarget);
        }
    }
}
