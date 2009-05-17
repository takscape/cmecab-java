package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.io.Reader;

import net.java.sen.StreamTagger;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

public class SenTokenizer extends Tokenizer
{
    private StreamTagger tagger;
    
    public SenTokenizer(Reader in, String confFile)
    throws IOException
    {
        super(in);
        tagger = new StreamTagger(in, confFile);
    }
    
    @Override
    public void close() throws IOException
    {
        super.close();
    }
    
    @Override
    public Token next(Token reusableToken) throws IOException
    {
        if (!tagger.hasNext()) {
            return null;
        }
        
        net.java.sen.Token token = tagger.next();
        if (token == null) {
            return next(reusableToken);
        }
        
        reusableToken.clear();
        reusableToken.setType(token.getPos());
        reusableToken.setTermBuffer(token.getSurface());
        reusableToken.setStartOffset(token.start());
        reusableToken.setEndOffset(token.end());

        return reusableToken;
    }
}
