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
