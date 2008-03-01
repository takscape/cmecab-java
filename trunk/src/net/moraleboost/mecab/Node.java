/*
**
**  Mar. 1, 2008
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
    
    /**
     * Nodeを構築。Tagger.parse()によって呼ばれる。ユーザが直接利用する機会はない。
     * @param hdl Nodeのハンドル
     * @param dec MeCabからの出力をデコードするためのデコーダ
     * @param enc MeCabへの入力をエンコードするためのエンコーダ
     * @throws CharacterCodingException 文字コードの変換エラーが発生
     * @throws MeCabException ネイティブライブラリ内部のエラー。
     */
    public Node(long hdl, CharsetDecoder dec, CharsetEncoder enc)
    throws CharacterCodingException, MeCabException
    {
    	prevHandle = 0;
        handle = hdl;
        decoder = dec;
        encoder = enc;
        
        nextMorpheme(); //BOSをスキップ
    }
    
    /**
     * Nodeを閉じる。close()呼び出し以降、hasNext()は常にfalseを返す。
     * また、next(), nxtMorpheme(), feature(), surface()は常にnullを返す。
     */
    public void close()
    {
    	prevHandle = 0;
        handle = 0;
    	surfaceCache = null;
    }
    
    /**
     * 次の形態素に移動し、その表層形を取得する。
     * Iterator<String>.next()を実装するメソッドであるため、例外はスローしない。
     * @return 次の形態素の表層形
     */
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
    
    /**
     * 次の形態素に移動し、その表層形を取得する。
     * @return 次の形態素の表層形
     * @throws CharacterCodingException 表層形をJava文字列にデコードできなかった
     * @throws MeCabException ネイティブライブラリの内部エラー
     */
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

    /**
     * 次の形態素が存在するかどうかを検査する。
     * @return 次の形態素が存在すればtrue、存在しなければfalse
     */
    public boolean hasNext()
    {
    	return (handle != 0);
    }
    
    /**
     * Iterator<String>.remove()を実装する。実際には何もしない。
     */
    public void remove()
    {
        // do nothing
    }
    
    /**
     * 現在の位置の形態素の素性情報を返す。
     * @return 素性情報
     * @throws CharacterCodingException 素性情報をJava文字列にデコードできなかった
     * @throws MeCabException ネイティブライブラリ内部のエラー
     */
    public String feature()
    throws CharacterCodingException, MeCabException
    {
    	if (prevHandle == 0) {
    		return null;
    	} else {
	        return CharsetUtil.decode(decoder, _feature(prevHandle));
    	}
    }
    
    /**
     * 現在の位置の形態素の表層形を返す。
     * @return 表層形
     */
    public String surface()
    {
    	return surfaceCache;
    }
    
    private static native byte[] _surface(long hdl);
    private static native byte[] _feature(long hdl);
    private static native long _next(long hdl);
}
