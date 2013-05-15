/*
 **
 **  Mar. 24, 2009
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

import java.io.IOException;
import java.io.Reader;

import net.moraleboost.io.BasicCodePointReader;
import net.moraleboost.tinysegmenter.TinySegmenter;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * {@link TinySegmenter}を用いて入力を分かち書きするTokenizer。
 * 
 * @author taketa
 *
 */
public class TinySegmenterTokenizer extends Tokenizer
{
    public static final String TOKENTYPE_WORD = "word".intern();

    private TinySegmenter segmenter;
    private long lastOffset;
    
    /**
     * トークンのターム属性
     */
    private CharTermAttribute termAttribute;
    /**
     * トークンのオフセット属性
     */
    private OffsetAttribute offsetAttribute;
    /**
     * トークンのタイプ属性
     */
    private TypeAttribute typeAttribute;

    public TinySegmenterTokenizer(Reader in)
    {
        this(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, in);
    }

    public TinySegmenterTokenizer(AttributeFactory factory, Reader in)
    {
        super(factory, in);
        
        termAttribute = addAttribute(CharTermAttribute.class);
        offsetAttribute = addAttribute(OffsetAttribute.class);
        typeAttribute = addAttribute(TypeAttribute.class);
        
        segmenter = new TinySegmenter(new BasicCodePointReader(in));
    }
    
    @Override
    public final boolean incrementToken() throws IOException
    {
        TinySegmenter.Token baseToken = segmenter.next();
        
        if (baseToken == null) {
            return false;
        }

        termAttribute.setEmpty();
        termAttribute.append(baseToken.str);
        offsetAttribute.setOffset(
                correctOffset((int)baseToken.start),
                correctOffset((int)baseToken.end));
        typeAttribute.setType(TOKENTYPE_WORD);
        
        lastOffset = baseToken.end;
        
        return true;
    }
    
    @Override
    public void end()
    {
        int finalOffset = correctOffset((int)lastOffset);
        offsetAttribute.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void reset()
    {
        segmenter = new TinySegmenter(new BasicCodePointReader(input));
        clearAttributes();
    }
}
