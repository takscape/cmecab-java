package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.solr.analysis.CharStream;

import net.moraleboost.lucene.analysis.ja.StandardMeCabTokenizer;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;

public class CharStreamAwareStandardMeCabTokenizer extends
        StandardMeCabTokenizer
{
    public CharStreamAwareStandardMeCabTokenizer(Reader in, String dicCharset,
            String arg) throws MeCabException, IOException
    {
        super(in, dicCharset, arg);
    }

    public CharStreamAwareStandardMeCabTokenizer(Reader in, String dicCharset,
            String arg, int maxSize) throws MeCabException, IOException
    {
        super(in, dicCharset, arg, maxSize);
    }

    public CharStreamAwareStandardMeCabTokenizer(Reader in, Tagger tagger,
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
