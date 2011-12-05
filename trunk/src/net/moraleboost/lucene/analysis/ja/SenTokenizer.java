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

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

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
    private String confFile = null;
    private StreamTagger tagger = null;
    private int lastOffset = 0;
    
    /**
     * トークンのターム属性
     */
    private CharTermAttribute termAttribute = null;
    /**
     * トークンのオフセット属性
     */
    private OffsetAttribute offsetAttribute = null;
    /**
     * トークンのタイプ属性
     */
    private TypeAttribute typeAttribute = null;

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
        
        this.confFile = confFile;
        tagger = new StreamTagger(in, confFile);
        
        termAttribute = addAttribute(CharTermAttribute.class);
        offsetAttribute = addAttribute(OffsetAttribute.class);
        typeAttribute = addAttribute(TypeAttribute.class);
    }
    
    @Override
    public void close() throws IOException
    {
        super.close();
    }
    
    @Override
    public boolean incrementToken() throws IOException
    {
        if (!tagger.hasNext()) {
            return false;
        }

        clearAttributes();

        net.java.sen.Token token = tagger.next();
        if (token == null) {
            return incrementToken();
        }
        
        String tokenString = token.getSurface();
        termAttribute.setEmpty();
        termAttribute.append(tokenString);
        offsetAttribute.setOffset(
                correctOffset(token.start()),
                correctOffset(token.end()));
        typeAttribute.setType(token.getPos());
        
        lastOffset = token.end();
        
        return true;
    }

    @Override
    public void end()
    {
        int finalOffset = correctOffset(lastOffset);
        offsetAttribute.setOffset(finalOffset, finalOffset);
    }
    
    @Override
    public void reset(Reader in) throws IOException
    {
        super.reset(in);
        tagger = new StreamTagger(in, confFile);
        lastOffset = 0;
    }
}
