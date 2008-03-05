package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class MeCabAnalyzer extends Analyzer
{
	private String dicCharset = null;
	private String mecabArg = null;
	
	public MeCabAnalyzer(String dicCharset, String mecabArg)
	{
		super();
		this.dicCharset = dicCharset;
		this.mecabArg = mecabArg;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader)
	{
		try {
			return new MeCabTokenizer(reader, dicCharset, mecabArg);
		} catch (IOException e) {
			return null;
		}
	}
}
