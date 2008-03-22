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
package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class MeCabAnalyzer extends Analyzer
{
	private String dicCharset = null;
	private String mecabArg = null;
	private String[] stopPatterns = null;
	
	public MeCabAnalyzer(String dicCharset, String mecabArg)
	{
		super();
		this.dicCharset = dicCharset;
		this.mecabArg = mecabArg;
	}

	public MeCabAnalyzer(String dicCharset, String mecabArg, String[] stopPatterns)
	{
		super();
		this.dicCharset = dicCharset;
		this.mecabArg = mecabArg;
		this.stopPatterns = stopPatterns;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader)
	{
		try {
			TokenStream stream = new MeCabTokenizer(reader, dicCharset, mecabArg);
			
			if (stopPatterns != null) {
				stream = new FeatureRegexFilter(stream, stopPatterns);
			}

			return stream;
		} catch (IOException e) {
			throw new MeCabTokenizerException(e);
		}
	}
}
