/*
 **
 **  May. 17, 2009
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

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

/**
 * MeCabを用いて入力を分かち書きするTokenizerのベース。
 * <br><br>
 * 生成されるTokenのtermには、形態素の表層形が格納される。
 * typeには、形態素の素性が格納される。
 * 
 * @author taketa
 *
 */
public abstract class MeCabTokenizer extends Tokenizer
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    private int maxSize = DEFAULT_MAX_SIZE;

    private Tagger tagger = null;
    private Node node = null;
    private int offset = 0;

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

    /**
     * オブジェクトを構築する。
     * 
     * @param in 入力
     * @param tagger 形態素解析器
     * @param maxSize 入力から読み込む最大文字数(in chars)
     * @throws MeCabException
     * @throws IOException
     */
    protected MeCabTokenizer(Reader in, Tagger tagger, int maxSize)
    throws MeCabException, IOException
    {
        super(in);

        this.maxSize = maxSize;
        
        this.tagger = tagger;

        termAttribute = (TermAttribute)addAttribute(TermAttribute.class);
        offsetAttribute = (OffsetAttribute)addAttribute(OffsetAttribute.class);
        typeAttribute = (TypeAttribute)addAttribute(TypeAttribute.class);
        
        parse();
    }
    
    protected Tagger getTagger()
    {
        return tagger;
    }
    
    @Override
    public boolean incrementToken() throws MeCabException, IOException
    {
        if (node == null || !node.hasNext()) {
            return false;
        }

        clearAttributes();
        
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
        
        termAttribute.setTermBuffer(tokenString);
        termAttribute.setTermLength(tokenString.length());
        offsetAttribute.setOffset(
                correctOffset(start),
                correctOffset(end));
        typeAttribute.setType(node.feature());
        
        return true;
    }
    
    @Override
    public void end()
    {
        int finalOffset = correctOffset(offset);
        offsetAttribute.setOffset(finalOffset, finalOffset);
    }
    
    @Override
    public void reset() throws IOException
    {
        offset = 0;
        node = tagger.reset();
    }
    
    @Override
    public void reset(Reader in) throws IOException
    {
        super.reset(in); // this.input = in;
        offset = 0;
        node = null;
        parse();
    }

    private void parse() throws MeCabException, IOException
    {
        // drain input
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        StringBuilder builder = new StringBuilder(DEFAULT_BUFFER_SIZE);
        long total = 0;
        int nread = 0;
        while (-1 != (nread = input.read(buffer))) {
            builder.append(buffer, 0, nread);
            total += nread;
            if (total > maxSize) {
                throw new MeCabException("Max size exceeded.");
            }
        }

        // parse
        node = tagger.parse(builder);
    }
}
