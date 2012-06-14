package net.moraleboost.lucene.analysis.ja;


import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.nio.CharBuffer;

public class TermRegexFilter extends AbstractRegexFilter
{
    private CharTermAttribute termAttribute = null;
    private PositionIncrementAttribute posIncAttribute = null;

    public TermRegexFilter(TokenStream input, String[] stopPatterns)
    {
        super(input, stopPatterns);
        termAttribute = addAttribute(CharTermAttribute.class);
        posIncAttribute = addAttribute(PositionIncrementAttribute.class);
    }

    @Override
    public final boolean incrementToken() throws IOException
    {
        int skippedPositions = 0;
        while (input.incrementToken()) {
            if (!match(CharBuffer.wrap(termAttribute.buffer(), 0, termAttribute.length()))) {
                posIncAttribute.setPositionIncrement(
                        posIncAttribute.getPositionIncrement() + skippedPositions);
                return true;
            }
            skippedPositions += posIncAttribute.getPositionIncrement();
        }

        return false;
    }

    public void reset() throws IOException
    {
        super.reset();
        clearAttributes();
    }
}
