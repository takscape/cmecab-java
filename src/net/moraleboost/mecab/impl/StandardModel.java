package net.moraleboost.mecab.impl;

import net.moraleboost.mecab.Model;
import org.bridj.BridJ;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.ann.Library;

import java.nio.charset.Charset;

@Library("mecab")
public class StandardModel implements Model
{
    static {
        if (Platform.isWindows()) {
            BridJ.setNativeLibraryActualName("mecab", "libmecab");
        }
        BridJ.register();
    }

    private static native Pointer<?> mecab_model_new2(Pointer<Byte> arg);
    private static native void mecab_model_destroy(Pointer<?> pModel);
    private static native Pointer<?> mecab_model_new_tagger(Pointer<?> pModel);
    private static native Pointer<?> mecab_model_new_lattice(Pointer<?> pModel);
    private static native int mecab_model_swap(Pointer<?> pModel, Pointer<?> pNewModel);
    private static native Pointer<StandardDictionaryInfo> mecab_model_dictionary_info(Pointer<?> pModel);

    private Pointer<?> pModel;
    private Charset charset;

    public StandardModel(String arg)
    {
        Pointer<Byte> parg = Pointer.pointerToCString(arg);
        try {
            pModel = mecab_model_new2(parg);
        } finally {
            Pointer.release(parg);
        }

        if (pModel == null) {
            throw new OutOfMemoryError("mecab_model_new2() failed.");
        }

        StandardDictionaryInfo dictInfo = dictionaryInfo();
        charset = Charset.forName(dictInfo.charset());
    }

    public StandardModel(String arg, Charset charset)
    {
        Pointer<Byte> parg = Pointer.pointerToCString(arg);
        try {
            pModel = mecab_model_new2(parg);
        } finally {
            Pointer.release(parg);
        }

        if (pModel == null) {
            throw new OutOfMemoryError("mecab_model_new2() failed.");
        }

        this.charset = charset;
    }

    protected Pointer<?> getPointer()
    {
        return pModel;
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
            if (pModel != null) {
                mecab_model_destroy(pModel);
            }
        } finally {
            pModel = null;
        }
    }

    public StandardTagger createTagger()
    {
        Pointer<?> p = mecab_model_new_tagger(pModel);
        if (p == null) {
            throw new OutOfMemoryError("mecab_model_new_tagger() failed.");
        } else {
            return new StandardTagger(p, charset);
        }
    }

    public StandardLattice createLattice()
    {
        Pointer<?> p = mecab_model_new_lattice(pModel);
        if (p == null) {
            throw new OutOfMemoryError("mecab_model_new_lattice() failed.");
        } else {
            return new StandardLattice(p, charset);
        }
    }

    public boolean swap(Model model)
    {
        if (model != null && (model instanceof StandardModel)) {
            return (mecab_model_swap(pModel, ((StandardModel)model).getPointer()) != 0);
        } else {
            return false;
        }
    }

    public StandardDictionaryInfo dictionaryInfo()
    {
        Pointer<StandardDictionaryInfo> p = mecab_model_dictionary_info(pModel);
        if (p == null) {
            throw new OutOfMemoryError("mecab_model_dictionary_info() failed.");
        } else {
            return new StandardDictionaryInfo(p);
        }
    }
}
