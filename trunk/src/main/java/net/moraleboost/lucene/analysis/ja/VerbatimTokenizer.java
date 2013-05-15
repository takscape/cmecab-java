package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 * 入力を単一のトークンとして出力するTokenizer
 * 
 * @author taketa
 *
 */
public class VerbatimTokenizer extends Tokenizer
{
    public static final int DEFAULT_BUFFER_SIZE = 256;
    
    /**
     * バッファ
     */
    private char[] buffer;
    /**
     * トークンのターム属性
     */
    private CharTermAttribute termAttribute;
    /**
     * トークンのオフセット属性
     */
    private OffsetAttribute offsetAttribute;
    
    public VerbatimTokenizer(Reader in)
    {
        this(in, DEFAULT_BUFFER_SIZE);
    }
    
    public VerbatimTokenizer(Reader in, int bufferSize)
    {
        this(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, in, bufferSize);
    }

    public VerbatimTokenizer(AttributeFactory factory, Reader in, int bufferSize)
    {
        super(factory, in);

        buffer = new char[bufferSize];
        termAttribute = addAttribute(CharTermAttribute.class);
        offsetAttribute = addAttribute(OffsetAttribute.class);
    }
    
    public final boolean incrementToken() throws IOException
    {
        clearAttributes();
        
        int nread = input.read(buffer, 0, buffer.length);
        if (nread <= 0) {
            return false;
        } else if (nread < buffer.length) {
            termAttribute.resizeBuffer(nread);
            termAttribute.copyBuffer(buffer, 0, nread);
            termAttribute.setLength(nread);
            offsetAttribute.setOffset(correctOffset(0), correctOffset(nread));
            return true;
        } else {
            StringBuilder builder = new StringBuilder(buffer.length * 2);
            builder.append(buffer, 0, nread);
            while ((nread=input.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, nread);
            }
            termAttribute.setEmpty();
            termAttribute.append(builder);
            offsetAttribute.setOffset(correctOffset(0), builder.length());
            return true;
        }
    }

    public void reset() throws IOException
    {
        super.reset();
        clearAttributes();
    }
}
