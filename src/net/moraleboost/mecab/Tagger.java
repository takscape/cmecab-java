package net.moraleboost.mecab;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

public class Tagger
{
	static {
		System.loadLibrary("CMeCab");
	}
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("MeCab version is " + Tagger.version());
		System.out.println();
		
		String text = args[1];
		System.out.println("Original text: " + text);
		System.out.println();
		
		System.out.println("Morphemes:");
		Tagger tagger = new Tagger(args[0], "");
		Node node = tagger.parse(text);
		while (node.hasNext()) {
			String surface = node.next();
			String feature = node.feature();
			System.out.println(surface + "\t" + feature);
		}
	}
	
    private CharsetDecoder decoder = null;
    private CharsetEncoder encoder = null;
    private Node node = null;
    private long handle = 0;

    public Tagger(String dicCharset, String arg)
    throws MeCabException
    {
        decoder = CharsetUtil.createDecoder(dicCharset,
                CodingErrorAction.IGNORE, CodingErrorAction.IGNORE);
        encoder = CharsetUtil.createEncoder(dicCharset,
                CodingErrorAction.IGNORE, CodingErrorAction.IGNORE);

        handle = _create(arg.getBytes());
        if (handle == 0) {
            throw new MeCabException("Failed to create a tagger.");
        }
    }

    protected void finalize()
    {
        close();
    }

    public void close()
    {
        if (node != null) {
            node.close();
            node = null;
        }
        
        if (handle != 0) {
            _destroy(handle);
            handle = 0;
        }
    }

    public Node parse(CharSequence text)
    throws CharacterCodingException, MeCabException
    {
        // 前の解析結果のノードを無効化する
        if (node != null) {
            node.close();
            node = null;
        }
        
        // 新しいテキストを解析
        long nodehdl = _parse(handle, CharsetUtil.encode(encoder, text, false));
        if (nodehdl == 0) {
            throw new MeCabException("Failed to parse text.");
        }
        node = new Node(nodehdl, decoder, encoder);

        return node;
    }
    
    public static String version()
    {
        return new String(_version());
    }

    private static native long _create(byte[] arg);
    private static native void _destroy(long hdl);
    private static native long _parse(long hdl, byte[] str);
    private static native byte[] _version();
}
