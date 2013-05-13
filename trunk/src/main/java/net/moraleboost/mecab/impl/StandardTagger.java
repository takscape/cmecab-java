package net.moraleboost.mecab.impl;

import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.Tagger;
import org.bridj.BridJ;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.ann.Library;

import java.nio.charset.Charset;

@Library("mecab")
public class StandardTagger implements Tagger
{
    static {
        if (Platform.isWindows()) {
            BridJ.setNativeLibraryActualName("mecab", "libmecab");
        }
        BridJ.register();
    }

    private static native Pointer<?> mecab_new2(Pointer<Byte> arg);
    private static native Pointer<Byte> mecab_version();
    private static native Pointer<Byte> mecab_strerror(Pointer<?> pTagger);
    private static native void mecab_destroy(Pointer<?> pTagger);
    private static native int mecab_parse_lattice(Pointer<?> pTagger, Pointer<?> pLattice);
    private static native Pointer<StandardDictionaryInfo> mecab_dictionary_info(Pointer<?> pTagger);

    private Pointer<?> pTagger;
    private Charset charset;

    public StandardTagger(String arg)
    {
        Pointer<Byte> parg = Pointer.pointerToCString(arg);
        try {
            pTagger = mecab_new2(parg);
        } finally {
            Pointer.release(parg);
        }

        if (pTagger == null) {
            throw new OutOfMemoryError("mecab_new2() failed.");
        }

        StandardDictionaryInfo dictInfo = dictionaryInfo();
        charset = Charset.forName(dictInfo.charset());
    }

    public StandardTagger(String arg, Charset charset)
    {
        Pointer<Byte> parg = Pointer.pointerToCString(arg);
        try {
            pTagger = mecab_new2(parg);
        } finally {
            Pointer.release(parg);
        }

        if (pTagger == null) {
            throw new OutOfMemoryError("mecab_new2() failed.");
        }

        this.charset = charset;
    }

    protected StandardTagger(Pointer<?> p, Charset charset)
    {
        this.pTagger = p;
        this.charset = charset;
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
            if (pTagger != null) {
                mecab_destroy(pTagger);
            }
        } finally {
            pTagger = null;
        }
    }

    public StandardLattice createLattice()
    {
        return new StandardLattice(charset);
    }

    public boolean parse(Lattice lattice)
    {
        if (lattice != null && (lattice instanceof StandardLattice)) {
            return (mecab_parse_lattice(pTagger, ((StandardLattice)lattice).getPointer()) != 0);
        } else {
            return false;
        }
    }

    public StandardDictionaryInfo dictionaryInfo()
    {
        Pointer<StandardDictionaryInfo> p = mecab_dictionary_info(pTagger);
        if (p == null) {
            throw new OutOfMemoryError("mecab_dictionary_info() failed.");
        } else {
            return new StandardDictionaryInfo(p);
        }
    }

    public String what()
    {
        Pointer<Byte> p = mecab_strerror(pTagger);
        if (p == null) {
            return null;
        } else {
            return p.getString(Pointer.StringType.C, charset);
        }
    }

    public String version()
    {
        Pointer<Byte> p = mecab_version();
        if (p == null) {
            return null;
        } else {
            return p.getCString();
        }
    }

    public static void main(String[] args)
    {
        StringBuilder text = new StringBuilder();
        for (String arg: args) {
            if (text.length() != 0) {
                text.append(" ");
            }
            text.append(arg);
        }

        StandardTagger tagger = new StandardTagger("");
        Lattice lattice = tagger.createLattice();
        lattice.setSentence(text.toString());
        tagger.parse(lattice);

        System.out.println("MeCab version " + tagger.version());
        System.out.println();
        System.out.println("Original text: " + text.toString());
        System.out.println();
        System.out.println("Morphemes:");
        System.out.println(lattice.toString());
    }
}
