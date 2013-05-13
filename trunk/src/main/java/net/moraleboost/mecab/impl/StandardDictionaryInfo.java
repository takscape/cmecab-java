package net.moraleboost.mecab.impl;

import net.moraleboost.mecab.DictionaryInfo;
import org.bridj.BridJUtil;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;

import java.nio.charset.Charset;

public class StandardDictionaryInfo extends StructObject implements DictionaryInfo
{
    protected StandardDictionaryInfo(Pointer<StandardDictionaryInfo> p)
    {
        super(p);
    }

    @Field(0)
    public Pointer<Byte> _filename()
    {
        return BridJUtil.getPointerField(this, 0);
    }

    public String filename()
    {
        Pointer<Byte> p = _filename();
        if (p == null) {
            return null;
        }

        if (Platform.isWindows()) {
            // always UTF-8
            return p.getString(Pointer.StringType.C, Charset.forName("UTF-8"));
        } else {
            return p.getCString();
        }
    }

    @Field(1)
    public Pointer<Byte> _charset()
    {
        return BridJUtil.getPointerField(this, 1);
    }

    public String charset()
    {
        Pointer<Byte> p = _charset();
        return (p == null) ? null : p.getCString();
    }

    @Field(2)
    public int _size()
    {
        return this.io.getIntField(this, 2);
    }

    public long size()
    {
        // convert to long
        return (_size() & 0xffffffffL);
    }

    @Field(3)
    public int type()
    {
        return this.io.getIntField(this, 3);
    }

    @Field(4)
    public int _lsize()
    {
        return this.io.getIntField(this, 4);
    }

    public long lsize()
    {
        return (_lsize() & 0xffffffffL);
    }

    @Field(5)
    public int _rsize()
    {
        return this.io.getIntField(this, 5);
    }

    public long rsize()
    {
        return (_rsize() & 0xffffffffL);
    }

    @Field(6)
    public short _version()
    {
        return this.io.getShortField(this, 6);
    }

    public int version()
    {
        return (_version() & 0xffff);
    }

    @Field(7)
    public Pointer<StandardDictionaryInfo> _next()
    {
        return this.io.getPointerField(this, 7);
    }

    public StandardDictionaryInfo next()
    {
        Pointer<StandardDictionaryInfo> p = _next();
        return (p == null) ? null : new StandardDictionaryInfo(p);
    }
}
