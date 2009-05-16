package net.moraleboost.solr;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import net.moraleboost.lucene.analysis.ja.LocalProtobufMeCabTokenizer;
import net.moraleboost.lucene.analysis.ja.MeCabTokenizerException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenizerFactory;

public class LocalProtobufMeCabTokenizerFactory extends BaseTokenizerFactory
{
    private String mecabArg = null;
    private int maxSize = LocalProtobufMeCabTokenizer.DEFAULT_MAX_SIZE;

    public LocalProtobufMeCabTokenizerFactory()
    {
        super();
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
     * ファクトリを初期化する。 初期化パラメータとして、「arg」、「maxSize」をとる。<br>
     * 「arg」には、MeCabに与えるオプションを指定する。
     * 省略すると、空文字列とみなされる。<br>
     * 残りのパラメータの意味については、
     * {@link LocalProtobufMeCabTokenizer#LocalProtobufMeCabTokenizer(Reader, String, int)}
     * を参照。
     * 
     * @param args
     *            初期化パラメータ
     */
    public void init(Map<String, String> args)
    {
        super.init(args);
        String arg = args.get("arg");
        String maxSizeStr = args.get("maxSize");

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
            return new LocalProtobufMeCabTokenizer(reader, mecabArg, maxSize);
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}
