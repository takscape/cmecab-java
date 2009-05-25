package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.solr.analysis.CharStream;

import net.moraleboost.lucene.analysis.ja.LocalProtobufMeCabTokenizer;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;

public class CharStreamAwareLocalProtobufMeCabTokenizer extends
        LocalProtobufMeCabTokenizer
{
    public CharStreamAwareLocalProtobufMeCabTokenizer(Reader in, String arg)
    throws MeCabException, IOException
    {
        super(in, arg);
    }

    public CharStreamAwareLocalProtobufMeCabTokenizer(Reader in, String arg,
            int maxSize) throws MeCabException, IOException
    {
        super(in, arg, maxSize);
    }

    public CharStreamAwareLocalProtobufMeCabTokenizer(Reader in, Tagger tagger,
            boolean ownTagger, int maxSize) throws MeCabException, IOException
    {
        super(in, tagger, ownTagger, maxSize);
    }

    public Token next(Token reusableToken)
    throws IOException
    {
        return OffsetCorrector.correctOffset(
                (CharStream)input, super.next(reusableToken));
    }
}
