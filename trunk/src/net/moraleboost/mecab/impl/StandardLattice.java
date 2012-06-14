package net.moraleboost.mecab.impl;

import net.moraleboost.mecab.Lattice;
import org.bridj.BridJ;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.SizeT;
import org.bridj.ann.Library;

import java.nio.charset.Charset;

@Library("mecab")
public class StandardLattice implements Lattice
{
    static {
        if (Platform.isWindows()) {
            BridJ.setNativeLibraryActualName("mecab", "libmecab");
        }
        BridJ.register();
    }

    private static native Pointer<?> mecab_lattice_new();
    private static native void mecab_lattice_destroy(Pointer<?> pLattice);
    private static native void mecab_lattice_clear(Pointer<?> pLattice);
    private static native int mecab_lattice_is_available(Pointer<?> pLattice);
    private static native Pointer<StandardNode> mecab_lattice_get_bos_node(Pointer<?> pLattice);
    private static native Pointer<StandardNode> mecab_lattice_get_eos_node(Pointer<?> pLattice);
    private static native Pointer<StandardNode> mecab_lattice_get_begin_nodes(Pointer<?> pLattice, SizeT pos);
    private static native Pointer<StandardNode> mecab_lattice_get_end_nodes(Pointer<?> pLattice, SizeT pos);
    private static native Pointer<Byte> mecab_lattice_get_sentence(Pointer<?> pLattice);
    private static native void mecab_lattice_set_sentence(Pointer<?> pLattice, Pointer<Byte> sentence);
    private static native SizeT mecab_lattice_get_size(Pointer<?> pLattice);
    private static native double mecab_lattice_get_z(Pointer<?> pLattice);
    private static native void mecab_lattice_set_z(Pointer<?> pLattice, double Z);
    private static native double mecab_lattice_get_theta(Pointer<?> pLattice);
    private static native void mecab_lattice_set_theta(Pointer<?> pLattice, double theta);
    private static native int mecab_lattice_next(Pointer<?> pLattice);
    private static native int mecab_lattice_get_request_type(Pointer<?> pLattice);
    private static native int mecab_lattice_has_request_type(Pointer<?> pLattice, int requestType);
    private static native void mecab_lattice_set_request_type(Pointer<?> pLattice, int requestType);
    private static native void mecab_lattice_add_request_type(Pointer<?> pLattice, int requestType);
    private static native void mecab_lattice_remove_request_type(Pointer<?> pLattice, int requestType);
    private static native Pointer<Byte> mecab_lattice_tostr(Pointer<?> pLattice);
    private static native Pointer<Byte> mecab_lattice_nbest_tostr(Pointer<?> pLattice, SizeT N);
    private static native Pointer<Byte> mecab_lattice_strerror(Pointer<?> pLattice);

    private Pointer<?> pLattice;
    private Pointer<Byte> pSentence;
    private Charset charset;

    public StandardLattice(Charset charset)
    {
        pLattice = mecab_lattice_new();
        if (pLattice == null) {
            throw new OutOfMemoryError("mecab_lattice_new() failed.");
        }
        this.charset = charset;
    }

    protected StandardLattice(Pointer<?> p, Charset charset)
    {
        this.pLattice = p;
        this.charset = charset;
    }

    protected Pointer<?> getPointer()
    {
        return pLattice;
    }

    protected void finalize() throws Throwable
    {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    public void destroy()
    {
        try {
            if (pLattice != null) {
                mecab_lattice_destroy(pLattice);
            }
            if (pSentence != null) {
                pSentence.release();
            }
        } finally {
            pSentence = null;
            pLattice = null;
        }
    }

    public void clear()
    {
        try {
            mecab_lattice_clear(pLattice);
            if (pSentence != null) {
                pSentence.release();
            }
        } finally {
            pSentence = null;
        }
    }

    public boolean isAvailable()
    {
        return (mecab_lattice_is_available(pLattice) != 0);
    }

    public StandardNode bosNode()
    {
        Pointer<StandardNode> p = mecab_lattice_get_bos_node(pLattice);
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    public StandardNode eosNode()
    {
        Pointer<StandardNode> p = mecab_lattice_get_eos_node(pLattice);
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    public StandardNode beginNodes(long pos)
    {
        Pointer<StandardNode> p = mecab_lattice_get_begin_nodes(pLattice, SizeT.valueOf(pos));
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    public StandardNode endNodes(long pos)
    {
        Pointer<StandardNode> p = mecab_lattice_get_end_nodes(pLattice, SizeT.valueOf(pos));
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    public String sentence()
    {
        Pointer<Byte> p = mecab_lattice_get_sentence(pLattice);
        if (p == null) {
            return null;
        }

        return p.getString(Pointer.StringType.C, charset);
    }

    public void setSentence(String sentence)
    {
        try {
            if (pSentence != null) {
                pSentence.release();
            }
        } finally {
            pSentence = null;
        }
        pSentence = Pointer.pointerToString(sentence, Pointer.StringType.C, charset).as(Byte.class);
        mecab_lattice_set_sentence(pLattice, pSentence);
    }

    public long size()
    {
        return mecab_lattice_get_size(pLattice).longValue();
    }

    public double Z()
    {
        return mecab_lattice_get_z(pLattice);
    }

    public void setZ(double Z)
    {
        mecab_lattice_set_z(pLattice, Z);
    }

    public double theta()
    {
        return mecab_lattice_get_theta(pLattice);
    }

    public void setTheta(double theta)
    {
        mecab_lattice_set_theta(pLattice, theta);
    }

    public boolean next()
    {
        return (mecab_lattice_next(pLattice) != 0);
    }

    public int requestType()
    {
        return mecab_lattice_get_request_type(pLattice);
    }

    public boolean hasRequestType(int requestType)
    {
        return (mecab_lattice_has_request_type(pLattice, requestType) != 0);
    }

    public void setRequestType(int requestType)
    {
        mecab_lattice_set_request_type(pLattice, requestType);
    }

    public void addRequestType(int requestType)
    {
        mecab_lattice_add_request_type(pLattice, requestType);
    }

    public void removeRequestType(int requestType)
    {
        mecab_lattice_remove_request_type(pLattice, requestType);
    }

    @Override
    public String toString()
    {
        Pointer<Byte> p = mecab_lattice_tostr(pLattice);
        if (p == null) {
            return null;
        } else {
            return p.getString(Pointer.StringType.C, charset);
        }
    }

    public String enumNBestAsString(long N)
    {
        Pointer<Byte> p = mecab_lattice_nbest_tostr(pLattice, SizeT.valueOf(N));
        if (p == null) {
            return null;
        } else {
            return p.getString(Pointer.StringType.C, charset);
        }
    }

    public String what()
    {
        Pointer<Byte> p = mecab_lattice_strerror(pLattice);
        if (p == null) {
            return null;
        } else {
            return p.getString(Pointer.StringType.C, charset);
        }
    }
}
