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
