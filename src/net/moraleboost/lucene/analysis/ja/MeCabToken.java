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

import org.apache.lucene.analysis.Token;

public class MeCabToken extends Token
{
	private String feature = null;
	
	public MeCabToken(String text, String feature, int start, int end)
	{
		super(text, start, end);
		this.feature = feature;
	}
	
	public String getFeature()
	{
		return feature;
	}
	
	public void setFeatrue(String feature)
	{
		this.feature = feature;
	}
}
