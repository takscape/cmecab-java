package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.solr.analysis.CharStream;

import net.moraleboost.lucene.analysis.ja.TinySegmenterTokenizer;

public class CharStreamAwareTinySegmenterTokenizer extends
        TinySegmenterTokenizer
{
    public CharStreamAwareTinySegmenterTokenizer(Reader in)
    {
        super(in);
    }

    public Token next(Token reusableToken)
    throws IOException
    {
        return OffsetCorrector.correctOffset(
                (CharStream)input, super.next(reusableToken));
    }
}
