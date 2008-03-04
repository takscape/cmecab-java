package net.moraleboost.lucene.analysis.ja;

import java.io.Reader;
import java.io.IOException;
import java.nio.CharBuffer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Token;

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.Node;

public class MeCabTokenizer extends Tokenizer
{
	public static final int BUFFER_INITIAL_SIZE 	= 4096;
	public static final int BUFFER_SHRINK_THRESOLD 	= 5 * 1024 * 1024;
	public static final int BUFFER_SHRINK_TARGET 	= 1024 * 1024;
	public static final int BUFFER_MAX_SIZE 		= 10 * 1024 * 1024;
	
	private StringBuilder buffer = null;
	private CharBuffer tmpBuffer = null;
	private Tagger tagger = null;
	private Node node = null;
	
	public MeCabTokenizer(Reader in, String dicCharset, String arg) throws IOException
	{
		super(in);
		buffer = new StringBuilder(BUFFER_INITIAL_SIZE);
		tmpBuffer = CharBuffer.allocate(BUFFER_INITIAL_SIZE);
		tagger = new Tagger(dicCharset, arg);
		
		parse();
	}
	
	public void close() throws IOException
	{
		if (tagger != null) {
			tagger.close();
		}
		node = null;
		super.close();
	}
	
	public Token next() throws java.io.IOException
	{
		if (node == null || !node.hasNext()) {
			return null;
		}
		
		String tokenString = node.nextMorpheme();
		return new Token(tokenString, 0, tokenString.length());
	}
	
	private void parse() throws IOException
	{
		// drain input
		int nread = 0;
		buffer.setLength(0);
		tmpBuffer.clear();
		while ((nread=input.read(tmpBuffer)) > 0) {
			tmpBuffer.rewind();
			buffer.append(tmpBuffer, 0, nread);
			tmpBuffer.clear();
			if (buffer.length() < BUFFER_MAX_SIZE) {
				throw new MeCabException("Buffer overflow");
			}
		}
		
		// parse
		node = tagger.parse(buffer);
		
		// shrink buffer if exceeded BUFFER_SHRINK_THRESOLD
		if (buffer.length() > BUFFER_SHRINK_THRESOLD) {
			buffer = new StringBuilder(BUFFER_SHRINK_TARGET);
		}
	}
}
