/*
 **
 **  May. 17, 2009
 **
 **  The author disclaims copyright to this source code.
 **  In place of a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **
 **                                         Stolen from SQLite :-)
 **  Any feedback is welcome.
 **  Kohei TAKETA <k-tak@void.in>
 **
 */
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

    public String blank()
    {
        if (morpheme.hasBlank()) {
            return morpheme.getBlank();
        } else {
            return null;
        }
    }

    public String surface()
    {
        return morpheme.getSurface();
    }
    
    public int posid()
    {
        return morpheme.getPosid();
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
