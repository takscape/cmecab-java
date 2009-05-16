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
package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;
import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Token;

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

/**
 * Tokenのtypeに、featureを格納する。
 * @author taketa
 *
 */
public class MeCabTokenizer extends Tokenizer
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    private int maxSize = DEFAULT_MAX_SIZE;

    private StringBuilder charSequence = null;
    private Tagger tagger = null;
    private Node node = null;
    private int offset = 0;
    private boolean ownTagger = false;

    public MeCabTokenizer(Reader in, Tagger tagger, boolean ownTagger, int maxSize)
    throws MeCabException, IOException
    {
        super(in);

        this.maxSize = maxSize;
        
        charSequence = new StringBuilder(DEFAULT_BUFFER_SIZE);
        this.tagger = tagger;
        this.ownTagger = ownTagger;
        
        parse();
    }
    
    protected Tagger getTagger()
    {
        return tagger;
    }

    @Override
    public void close() throws IOException
    {
        if (ownTagger && tagger != null) {
            tagger.close();
        }
        node = null;

        super.close();
    }

    @Override
    public Token next(Token reusableToken) throws MeCabException, IOException
    {
        if (node == null || !node.hasNext()) {
            return null;
        }

        String tokenString = node.nextMorpheme();
        String blankString = node.blank();
        int start;
        int end;

        if (blankString != null) {
            start = offset + blankString.length();
            end = start + tokenString.length();
        } else {
            start = offset;
            end = start + tokenString.length();
        }

        offset = end;
        
        reusableToken.clear();
        reusableToken.setType(node.feature());
        reusableToken.setTermBuffer(tokenString);
        reusableToken.setStartOffset(start);
        reusableToken.setEndOffset(end);
        return reusableToken;
    }

    private void parse() throws MeCabException, IOException
    {
        // drain input
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long total = 0;
        int nread = 0;
        while (-1 != (nread = input.read(buffer))) {
            charSequence.append(buffer, 0, nread);
            total += nread;
            if (total > maxSize) {
                throw new MeCabException("Max size exceeded.");
            }
        }

        // parse
        node = tagger.parse(charSequence);
    }
}
