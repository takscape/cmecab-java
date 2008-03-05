package net.moraleboost.solr;

import java.io.Reader;
import java.io.IOException;
import java.nio.charset.Charset;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenizerFactory;

import net.moraleboost.lucene.analysis.ja.MeCabTokenizer;

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

		String charset = args.get("dicCharset");
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
			return null;
		}
	}
}
