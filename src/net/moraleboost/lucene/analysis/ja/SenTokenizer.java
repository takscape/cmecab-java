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

import java.io.IOException;
import java.io.Reader;

import net.java.sen.StreamTagger;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

/**
 * Senを用いて入力を分かち書きするTokenizer。
 * 形態素の品詞情報を、Tokenのtypeに格納する。
 * また表層形を、Tokenのtermに格納する。
 * 
 * @author taketa
 *
 */
public class SenTokenizer extends Tokenizer
{
    private StreamTagger tagger = null;
    
    /**
     * SenTokenizerを構築する。
     * 
     * @param in 入力
     * @param confFile Senの設定ファイル（sen.xml）のパス
     * @throws IOException
     */
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
        
        return reusableToken.reinit(
              token.getSurface(),
              token.start(),
              token.end(),
              token.getPos());
    }
}
