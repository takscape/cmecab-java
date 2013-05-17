/*
 **
 **  Mar. 5, 2008
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
package net.moraleboost.solr;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;
import net.moraleboost.lucene.analysis.ja.StandardMeCabTokenizer;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.StandardTagger;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * {@link StandardMeCabTokenizer}のファクトリ。
 * @author taketa
 *
 */
public class StandardMeCabTokenizerFactory extends TokenizerFactory
{
    private String dicCharset;
    private String mecabArg;
    private int maxSize;
    private Tagger tagger;

    public StandardMeCabTokenizerFactory(Map<String, String> args)
    {
        super(args);
        maxSize = StandardMeCabTokenizer.DEFAULT_MAX_SIZE;
        init(args);
    }
    
    public String getDicCharset()
    {
        return dicCharset;
    }
    
    public String getMecabArg()
    {
        return mecabArg;
    }
    
    public int getMaxSize()
    {
        return maxSize;
    }

    /**
     * ファクトリを初期化する。 初期化パラメータとして、「charset」、「arg」、
     * 「maxSize」をとる。<br>
     * 「charset」には、MeCabの辞書の文字コードを指定する。
     * 省略すると、辞書の文字コードが用いられる。<br>
     * 「arg」には、MeCabに与えるオプションを指定する。
     * 省略すると、空文字列とみなされる。<br>
     * 残りのパラメータの意味については、
     * {@link StandardMeCabTokenizer#StandardMeCabTokenizer(Reader, Tagger, int)}
     * を参照。
     * 
     * @param args
     *            初期化パラメータ
     */
    protected void init(Map<String, String> args)
    {
        String charset = args.get("charset");
        String arg = args.get("arg");
        String maxSizeStr = args.get("maxSize");

        if (charset != null) {
            dicCharset = charset;
        } else {
            dicCharset = null;
        }

        if (arg != null) {
            mecabArg = arg;
        } else {
            mecabArg = "";
        }

        if (maxSizeStr != null) {
            maxSize = Integer.parseInt(maxSizeStr);
        }

        if (dicCharset == null) {
            tagger = new StandardTagger(mecabArg);
        } else {
            tagger = new StandardTagger(mecabArg, Charset.forName(dicCharset));
        }
    }

    @Override
    public Tokenizer create(AttributeSource.AttributeFactory factory, Reader input)
    {
        try {
            return new StandardMeCabTokenizer(factory, input, tagger, maxSize);
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
