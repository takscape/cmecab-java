/*
 **
 **  May. 17, 2009
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

import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.impl.Messages.ParsingRequest;
import net.moraleboost.mecab.impl.Messages.ParsingResponse;

/**
 * ネイティブライブラリとの間の通信に、Protocol Buffersを用いるTagger。
 * MeCabの辞書の文字コードは、UTF-8でなければならない。
 * @author taketa
 *
 */
public class LocalProtobufTagger extends ProtobufTagger
{
    static {
        System.loadLibrary("CMeCab");
    }

    private long handle = 0;
    
    /**
     * オブジェクトを構築する。
     * 
     * @param arg MeCabに与えるオプション
     * @throws MeCabException
     */
    public LocalProtobufTagger(String arg)
    throws MeCabException
    {
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
        super.close();
        
        if (handle != 0) {
            _destroy(handle);
            handle = 0;
        }
    }

    @Override
    protected ParsingResponse parse(ParsingRequest request) throws Exception
    {
        return ParsingResponse.parseFrom(_parse(handle, request.toByteArray()));
    }
    
    /**
     * バージョン文字列を取得する
     * 
     * @return バージョン文字列
     */
    public static String version()
    {
        return new String(_version());
    }

    private static native long _create(byte[] arg);

    private static native void _destroy(long hdl);

    private static native byte[] _parse(long hdl, byte[] request);

    private static native byte[] _version();
}
