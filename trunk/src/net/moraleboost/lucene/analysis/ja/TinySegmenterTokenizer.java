package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.io.Reader;

import net.moraleboost.io.BasicCodePointReader;
import net.moraleboost.tinysegmenter.TinySegmenter;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

public class TinySegmenterTokenizer extends Tokenizer
{
    private TinySegmenter segmenter = null;
    
    public TinySegmenterTokenizer(Reader in)
    {
        super(in);
        segmenter = new TinySegmenter(new BasicCodePointReader(in));
    }
    
    public Token next() throws IOException
    {
        TinySegmenter.Token baseToken = segmenter.next();
        
        if (baseToken == null) {
            return null;
        } else {
            Token ret = new Token((int)baseToken.start, (int)baseToken.end);
            ret.setTermBuffer(baseToken.str);
            return ret;
        }
    }
}
