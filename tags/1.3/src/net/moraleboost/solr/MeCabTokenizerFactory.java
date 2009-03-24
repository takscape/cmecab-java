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

import net.moraleboost.lucene.analysis.ja.MeCabTokenizer;
import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;

public class MeCabTokenizerFactory extends BaseTokenizerFactory
{
    private String dicCharset = null;
    private String mecabArg = null;
    private int bufferSize = MeCabTokenizer.DEFAULT_BUFFER_SIZE;
    private int maxSize = MeCabTokenizer.DEFAULT_MAX_SIZE;

    public MeCabTokenizerFactory()
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
    
    public int getBufferSize()
    {
        return bufferSize;
    }
    public int getMaxSize()
    {
        return maxSize;
    }

    /**
     * ファクトリを初期化する。 初期化パラメータとして、「charset」、「arg」、
     * 「bufferSize」、「maxSize」をとる。<br>
     * 「charset」には、MeCabの辞書の文字コードを指定する。
     * 省略すると、Javaの既定文字コードが用いられる。<br>
     * 「arg」には、MeCabに与えるオプションを指定する。
     * 省略すると、空文字列とみなされる。<br>
     * 残りのパラメータの意味については、
     * {@link MeCabTokenizer#MeCabTokenizer(Reader, String, String, int, int)}
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
        String bufferSizeStr = args.get("bufferSize");
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

        if (bufferSizeStr != null) {
            bufferSize = Integer.parseInt(bufferSizeStr);
        }

        if (maxSizeStr != null) {
            maxSize = Integer.parseInt(maxSizeStr);
        }
    }

    public TokenStream create(Reader reader)
    {
        try {
            return new MeCabTokenizer(reader, dicCharset, mecabArg,
                    bufferSize, maxSize);
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
