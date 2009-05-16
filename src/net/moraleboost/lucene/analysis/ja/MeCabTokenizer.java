package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;
import java.io.IOException;
import java.nio.CharBuffer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Token;

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

public class MeCabTokenizer extends Tokenizer
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    private int maxSize = DEFAULT_MAX_SIZE;

    private StringBuilder charSequence = null;
    private CharBuffer buffer = null;
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
        buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
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
    public Token next() throws MeCabException, IOException
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
        return new MeCabToken(tokenString, node.feature(), start, end);
    }

    private void parse() throws MeCabException, IOException
    {
        // drain input
        int nread = 0;
        charSequence.setLength(0);
        buffer.clear();
        while ((nread = input.read(buffer)) > 0) {
            buffer.rewind();
            charSequence.append(buffer, 0, nread);
            buffer.clear();
            if (charSequence.length() > maxSize) {
                throw new MeCabException("Max size exceeded.");
            }
        }

        // parse
        node = tagger.parse(charSequence);
    }
}
