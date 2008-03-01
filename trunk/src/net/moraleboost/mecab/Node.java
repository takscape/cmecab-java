package net.moraleboost.mecab;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

public class Node implements Iterator<String>
{
    private CharsetDecoder decoder = null;
    private CharsetEncoder encoder = null;
    private long prevHandle = 0;
    private long handle = 0;
    private String surfaceCache = null;
    
    public Node(long hdl, CharsetDecoder dec, CharsetEncoder enc)
    throws CharacterCodingException, MeCabException
    {
    	prevHandle = 0;
        handle = hdl;
        decoder = dec;
        encoder = enc;
        
        nextMorpheme(); //BOSをスキップ
    }
    
    public void close()
    {
    	prevHandle = 0;
        handle = 0;
    	surfaceCache = null;
    }
    
    public String next()
    {
        try {
            return nextMorpheme();
        } catch (CharacterCodingException e) {
            // ignore
        } catch (MeCabException e) {
            // ignore
        }
        return null;
    }
    
    public String nextMorpheme()
    throws CharacterCodingException, MeCabException
    {
        if (handle == 0) {
            return null;
        }
        
        surfaceCache = CharsetUtil.decode(decoder, _surface(handle));
        prevHandle = handle;
        handle = _next(handle);

        return surfaceCache;
    }
    
    public boolean hasNext()
    {
    	return (handle != 0);
    }
    
    public void remove()
    {
        // do nothing
    }
    
    public String feature()
    throws CharacterCodingException, MeCabException
    {
    	if (prevHandle == 0) {
    		return null;
    	} else {
	        return CharsetUtil.decode(decoder, _feature(prevHandle));
    	}
    }
    
    public String surface()
    {
    	return surfaceCache;
    }
    
    private static native byte[] _surface(long hdl);
    private static native byte[] _feature(long hdl);
    private static native long _next(long hdl);
}
