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

import java.io.Reader;
import java.io.IOException;
import java.nio.charset.Charset;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenizerFactory;

import net.moraleboost.lucene.analysis.ja.StandardMeCabTokenizer;
import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;

/**
 * {@link StandardMeCabTokenizer}のファクトリ。
 * @author taketa
 *
 */
public class StandardMeCabTokenizerFactory extends BaseTokenizerFactory
{
    private String dicCharset = null;
    private String mecabArg = null;
    private int maxSize = StandardMeCabTokenizer.DEFAULT_MAX_SIZE;

    public StandardMeCabTokenizerFactory()
    {
        super();
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
     * 省略すると、Javaの既定文字コードが用いられる。<br>
     * 「arg」には、MeCabに与えるオプションを指定する。
     * 省略すると、空文字列とみなされる。<br>
     * 残りのパラメータの意味については、
     * {@link StandardMeCabTokenizer#StandardMeCabTokenizer(Reader, String, String, int)}
     * を参照。
     * 
     * @param args
     *            初期化パラメータ
     */
    public void init(Map<String, String> args)
    {
        super.init(args);

        String charset = args.get("charset");
        String arg = args.get("arg");
        String maxSizeStr = args.get("maxSize");

        if (charset != null) {
            dicCharset = charset;
        } else {
            dicCharset = Charset.defaultCharset().name();
        }

        if (arg != null) {
            mecabArg = arg;
        } else {
            mecabArg = "";
        }

        if (maxSizeStr != null) {
            maxSize = Integer.parseInt(maxSizeStr);
        }
    }

    public TokenStream create(Reader reader)
    {
        try {
            return new StandardMeCabTokenizer(reader, dicCharset, mecabArg, maxSize);
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
