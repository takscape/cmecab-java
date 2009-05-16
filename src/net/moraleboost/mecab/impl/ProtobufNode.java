package net.moraleboost.mecab.impl;

import java.util.Iterator;

import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.impl.Messages.ParsingResponse;
import net.moraleboost.mecab.impl.Messages.ParsingResponse.Morpheme;;

public class ProtobufNode implements Node
{
    private Iterator<Morpheme> iterator;
    private Morpheme morpheme;

    public ProtobufNode(ParsingResponse result)
    {
        this.iterator = result.getMorphemeList().iterator();
    }

    public void close()
    {
    }

    public String feature()
    {
        return morpheme.getFeature();
    }

    public String nextMorpheme()
    {
        morpheme = iterator.next();
        return morpheme.getSurface();
    }

    public String rsurface()
    {
        return morpheme.getRsurface();
    }

    public String surface()
    {
        return morpheme.getSurface();
    }

    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    public String next()
    {
        return nextMorpheme();
    }

    public void remove()
    {
    }
}
