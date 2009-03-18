package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;

import net.moraleboost.mecab.util.BasicCodePointReader;
import net.moraleboost.mecab.util.CodePointReader;

import org.apache.lucene.analysis.Tokenizer;

public class TinySegmenterTokenizer extends Tokenizer
{
    private CodePointReader reader = null;
    
    public TinySegmenterTokenizer(Reader in)
    {
        super(in);
        reader = new BasicCodePointReader(in);
    }
}
