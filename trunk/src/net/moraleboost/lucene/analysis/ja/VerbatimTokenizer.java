package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

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
    private TermAttribute termAttribute;
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
        super(in);
        buffer = new char[bufferSize];
        termAttribute = (TermAttribute)addAttribute(TermAttribute.class);
        offsetAttribute = (OffsetAttribute)addAttribute(OffsetAttribute.class);
    }
    
    public boolean incrementToken() throws IOException
    {
        clearAttributes();
        
        int nread = input.read(buffer, 0, buffer.length);
        if (nread <= 0) {
            return false;
        } else if (nread < buffer.length) {
            termAttribute.setTermBuffer(new String(buffer, 0, nread));
            offsetAttribute.setOffset(correctOffset(0), correctOffset(nread));
            return true;
        } else {
            StringBuilder builder = new StringBuilder(buffer.length * 2);
            builder.append(buffer, 0, nread);
            while ((nread=input.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, nread);
            }
            termAttribute.setTermBuffer(builder.toString());
            offsetAttribute.setOffset(correctOffset(0), builder.length());
            return true;
        }
    }
}
