package net.moraleboost.mecab.impl;

import net.moraleboost.io.CharsetUtil;
import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Node;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

public class StandardLattice implements Lattice
{
    private long handle = 0;
    private CharsetEncoder encoder = null;
    private CharsetDecoder decoder = null;

    public StandardLattice(String dicCharset, long handle) throws MeCabException
    {
        this.handle = handle;
        try {
            encoder = CharsetUtil.createEncoder(
                    dicCharset, CodingErrorAction.IGNORE, CodingErrorAction.IGNORE);
            decoder = CharsetUtil.createDecoder(
                    dicCharset, CodingErrorAction.IGNORE, CodingErrorAction.IGNORE);
        } catch (RuntimeException re) {
            close();
            throw re;
        } catch (Exception e) {
            close();
            throw new MeCabException(e);
        }
    }

    protected long getHandle()
    {
        return handle;
    }

    public void close()
    {
        if (handle != 0) {
            _destroy(handle);
            handle = 0;
        }
    }

    protected void finalize()
    {
        close();
    }

    public void setSentence(CharSequence sentence) throws CharacterCodingException
    {
        byte[] byteSentence = CharsetUtil.encode(encoder, sentence, false);
        _setSentence(handle, byteSentence);
    }

    public Node bosNode() throws MeCabException, CharacterCodingException
    {
        return new StandardNode(_bosNode(handle), decoder);
    }

    public Node eosNode() throws MeCabException, CharacterCodingException
    {
        return new StandardNode(_eosNode(handle), decoder);
    }

    private static native void _destroy(long hdl);
    private static native void _setSentence(long hdl, byte[] sentence);
    private static native long _bosNode(long hdl);
    private static native long _eosNode(long hdl);
}
