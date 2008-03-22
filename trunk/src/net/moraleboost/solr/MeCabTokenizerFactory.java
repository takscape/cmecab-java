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
    private int initialSize = MeCabTokenizer.DEFAULT_BUFFER_INITIAL_SIZE;
    private int shrinkThreshold = MeCabTokenizer.DEFAULT_BUFFER_SHRINK_THRESHOLD;
    private int shrinkTarget = MeCabTokenizer.DEFAULT_BUFFER_SHRINK_TARGET;
    private int maxSize = MeCabTokenizer.DEFAULT_BUFFER_MAX_SIZE;
    
    public MeCabTokenizerFactory()
    {
        super();
    }

    /**
     * ファクトリを初期化する。 初期化パラメータとして、「charset」、「arg」、
     * 「initialSize」、「shrinkThreshold」、「shrinkTarget」、「maxSize」をとる。<br>
     * 「charset」には、MeCabの辞書の文字コードを指定する。省略すると、Javaの既定文字コードが用いられる。<br>
     * 「arg」には、MeCabに与えるオプションを指定する。省略すると、空文字列とみなされる。<br>
     * 残りのパラメータの意味については、
     * {@link MeCabTokenizer#MeCabTokenizer(Reader, String, String, int, int, int, int)}を参照。
     * @param args 初期化パラメータ
     */
    public void init(Map<String, String> args)
    {
        super.init(args);

        String charset = args.get("charset");
        String arg = args.get("arg");
        String initialSizeStr = args.get("initialSize");
        String shrinkThresholdStr = args.get("shrinkThreshold");
        String shrinkTargetStr = args.get("shrinkTarget");
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
        
        if (initialSizeStr != null) {
            initialSize = Integer.parseInt(initialSizeStr);
        }
        
        if (shrinkThresholdStr != null) {
            shrinkThreshold = Integer.parseInt(shrinkThresholdStr);
        }
        
        if (shrinkTargetStr != null) {
            shrinkTarget = Integer.parseInt(shrinkTargetStr);
        }
        
        if (maxSizeStr != null) {
            maxSize = Integer.parseInt(maxSizeStr);
        }
    }

    public TokenStream create(Reader reader)
    {
        try {
            return new MeCabTokenizer(reader, dicCharset, mecabArg);
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
