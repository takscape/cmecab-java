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
package net.moraleboost.mecab.impl;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import net.moraleboost.io.CharsetUtil;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Node;

/**
 * {@link StandardTagger}の返すNode。
 * @author taketa
 *
 */
public class StandardNode implements Node
{
    private CharsetDecoder decoder = null;
    @SuppressWarnings("unused")
    private CharsetEncoder encoder = null;
    private long prevHandle = 0;
    private long handle = 0;
    private String surfaceCache = null;
    private String blankCache = null;

    /**
     * オブジェクトを構築する。StandardTagger.parse()によって呼ばれる。
     * ユーザが直接利用する機会はない。
     * 
     * @param hdl
     *            Nodeのハンドル
     * @param dec
     *            MeCabからの出力をデコードするためのデコーダ
     * @param enc
     *            MeCabへの入力をエンコードするためのエンコーダ
     * @throws CharacterCodingException
     *             文字コードの変換エラーが発生
     * @throws MeCabException
     *             ネイティブライブラリ内部のエラー。
     */
    public StandardNode(long hdl, CharsetDecoder dec, CharsetEncoder enc)
    throws CharacterCodingException, MeCabException
    {
        prevHandle = 0;
        handle = hdl;
        decoder = dec;
        encoder = enc;

        nextMorpheme(); // BOSをスキップ
    }

    public void close()
    {
        prevHandle = 0;
        handle = 0;
        surfaceCache = null;
        blankCache = null;
    }

    /**
     * 次の形態素に移動し、その表層形を取得する。
     * Iterator.next()を実装するメソッドであるため、例外はスローしない。
     * 
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

    public String nextMorpheme()
    throws CharacterCodingException, MeCabException
    {
        if (handle == 0) {
            return null;
        }

        surfaceCache = CharsetUtil.decode(decoder, _surface(handle));
        byte[] blank = _blank(handle);
        if (blank != null) {
            blankCache = CharsetUtil.decode(decoder, blank);
        } else {
            blankCache = null;
        }
        prevHandle = handle;
        handle = _next(handle);

        return surfaceCache;
    }

    /**
     * 次の形態素が存在するかどうかを検査する。
     * 
     * @return 次の形態素が存在すればtrue、存在しなければfalse
     */
    public boolean hasNext()
    {
        return (handle != 0);
    }

    /**
     * Iterator.remove()を実装する。実際には何もしない。
     */
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

    public String blank()
    {
        return blankCache;
    }
    
    public int posid()
    {
        if (prevHandle == 0) {
            return 0;
        } else {
            return _posid(prevHandle);
        }
    }

    private static native byte[] _surface(long hdl);

    private static native byte[] _blank(long hdl);

    private static native byte[] _feature(long hdl);
    
    private static native int _posid(long hdl);

    private static native long _next(long hdl);
}
