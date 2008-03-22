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

    public MeCabTokenizerFactory()
    {
        super();
    }

    public void init(Map<String, String> args)
    {
        super.init(args);

        String charset = args.get("charset");
        String arg = args.get("arg");
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
