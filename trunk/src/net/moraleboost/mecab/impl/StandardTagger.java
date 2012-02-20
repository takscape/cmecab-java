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
import java.nio.charset.CodingErrorAction;

import net.moraleboost.io.CharsetUtil;
import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

/**
 * JNIを用いてMeCabを呼び出すTagger。
 * 
 * @author taketa
 *
 */
public class StandardTagger implements Tagger
{
    private String dicCharset = null;
    private long handle = 0;

    /**
     * 形態素解析器を構築する。
     * 
     * @param dicCharset
     *            MeCabの辞書の文字コード。WindowsではShift_JIS、UNIX系ではEUC-JPであることが多いであろう。
     * @param handle
     *            Taggerのハンドル。
     * @throws MeCabException
     *             ネイティブライブラリの内部エラー
     */
    protected StandardTagger(String dicCharset, long handle)
    {
        this.dicCharset = dicCharset;
        this.handle = handle;
    }

    protected void finalize()
    {
        close();
    }

    public void close()
    {
        if (handle != 0) {
            _destroy(handle);
            handle = 0;
        }
    }

    public void parse(Lattice lattice) throws CharacterCodingException,
            MeCabException
    {
        StandardLattice standardLattice = (StandardLattice)lattice;
        if (!_parse(standardLattice.getHandle())) {
            throw new MeCabException("Failed to parse text.");
        }
    }

    private static native void _destroy(long hdl);
    private static native boolean _parse(long hdl);
}
