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

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;

import net.moraleboost.mecab.impl.Messages.ParsingRequest;
import net.moraleboost.mecab.impl.Messages.ParsingResponse;

public abstract class ProtobufTagger implements Tagger
{
    public ProtobufNode parse(CharSequence text)
    throws MeCabException
    {
        ParsingRequest req;
        
        if (text instanceof String) {
            req = ParsingRequest.newBuilder()
                .setText((String)text)
                .build();
        } else {
            req = ParsingRequest.newBuilder()
                .setText(text.toString())
                .build();
        }

        try {
            return new ProtobufNode(parse(req));
        } catch (Exception e) {
            throw new MeCabException(e);
        }
    }
    
    protected abstract ParsingResponse parse(ParsingRequest request) throws Exception;
}
