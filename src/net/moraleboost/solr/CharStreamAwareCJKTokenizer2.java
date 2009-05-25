package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.solr.analysis.CharStream;

import net.moraleboost.lucene.analysis.ja.CJKTokenizer2;

public class CharStreamAwareCJKTokenizer2 extends CJKTokenizer2
{
    public CharStreamAwareCJKTokenizer2(Reader in)
    {
        super(in);
    }

    public CharStreamAwareCJKTokenizer2(Reader in, int ngram)
    {
        super(in, ngram);
    }
    
    public Token next(Token reusableToken)
    throws IOException
    {
        return OffsetCorrector.correctOffset(
                (CharStream)input, super.next(reusableToken));
    }
}
