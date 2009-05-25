package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.solr.analysis.CharStream;

import net.moraleboost.lucene.analysis.ja.SenTokenizer;

public class CharStreamAwareSenTokenizer extends SenTokenizer
{
    public CharStreamAwareSenTokenizer(Reader in, String confFile)
            throws IOException
    {
        super(in, confFile);
    }

    public Token next(Token reusableToken)
    throws IOException
    {
        return OffsetCorrector.correctOffset(
                (CharStream)input, super.next(reusableToken));
    }
}
