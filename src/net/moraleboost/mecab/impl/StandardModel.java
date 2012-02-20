package net.moraleboost.mecab.impl;

import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.MeCabException;
import net.moraleboost.mecab.Model;
import net.moraleboost.mecab.Tagger;

public class StandardModel implements Model
{
    static {
        System.loadLibrary("CMeCab");
    }

    String dicCharset = null;
    private long handle = 0;

    public StandardModel(String dicCharset, String arg) throws MeCabException
    {
        this.dicCharset = dicCharset;
        handle = _create(arg.getBytes());
        if (handle == 0) {
            throw new MeCabException("Failed to create a model.");
        }
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
    
    public Tagger createTagger() throws MeCabException
    {
        long taggerHandle = _createTagger();
        if (taggerHandle == 0) {
            throw new MeCabException("Failed to create a tagger.");
        }
        return new StandardTagger(dicCharset, taggerHandle);
    }

    public Lattice createLattice() throws MeCabException
    {
        long latticeHandle = _createLattice();
        if (latticeHandle == 0) {
            throw new MeCabException("Failed to create a lattice.");
        }
        return new StandardLattice(dicCharset, latticeHandle);
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
    private static native long _createTagger();
    private static native long _createLattice();
    private static native byte[] _version();
}
