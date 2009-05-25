package net.moraleboost.solr;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.solr.analysis.CharStream;

public abstract class OffsetCorrector
{
    public static Token correctOffset(CharStream cs, Token token)
    {
        if (token != null) {
            token.setStartOffset(cs.correctOffset(token.startOffset()));
            token.setEndOffset(cs.correctOffset(token.endOffset()));
        }

        return token;
    }
}
