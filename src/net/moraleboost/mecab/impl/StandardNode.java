package net.moraleboost.mecab.impl;

import net.moraleboost.mecab.Node;
import org.bridj.BridJUtil;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.CLong;
import org.bridj.ann.Field;

import java.nio.charset.Charset;

public class StandardNode extends StructObject implements Node
{
    private Charset charset;

    protected StandardNode(Pointer<StandardNode> p, Charset charset)
    {
        super(p);
        this.charset = charset;
    }

    @Field(0)
    public Pointer<StandardNode> _prev()
    {
        return BridJUtil.getPointerField(this, 0);
    }

    public StandardNode prev()
    {
        Pointer<StandardNode> p = _prev();
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    @Field(1)
    public Pointer<StandardNode> _next()
    {
        return BridJUtil.getPointerField(this, 1);
    }

    public StandardNode next()
    {
        Pointer<StandardNode> p = _next();
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    @Field(2)
    public Pointer<StandardNode> _enext()
    {
        return BridJUtil.getPointerField(this, 2);
    }

    public StandardNode enext()
    {
        Pointer<StandardNode> p = _enext();
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    @Field(3)
    public Pointer<StandardNode> _bnext()
    {
        return BridJUtil.getPointerField(this, 3);
    }

    public StandardNode bnext()
    {
        Pointer<StandardNode> p = _bnext();
        if (p == null) {
            return null;
        } else {
            return new StandardNode(p, charset);
        }
    }

    @Field(4)
    public Pointer<StandardPath> _rpath()
    {
        return BridJUtil.getPointerField(this, 4);
    }

    public StandardPath rpath()
    {
        Pointer<StandardPath> p = _rpath();
        if (p == null) {
            return null;
        } else {
            return new StandardPath(p, charset);
        }
    }

    @Field(5)
    public Pointer<StandardPath> _lpath()
    {
        return BridJUtil.getPointerField(this, 5);
    }

    public StandardPath lpath()
    {
        Pointer<StandardPath> p = _lpath();
        if (p == null) {
            return null;
        } else {
            return new StandardPath(p, charset);
        }
    }

    @Field(6)
    public Pointer<Byte> _surface()
    {
        return BridJUtil.getPointerField(this, 6);
    }

    public String surface()
    {
        Pointer<Byte> p = _surface();
        int len = length();
        if (p == null) {
            return null;
        } else {
            return new String(p.getBytes(len), charset);
        }
    }

    public String rsurface()
    {
        Pointer<Byte> p = _surface();
        int rlen = rlength();
        int len = length();
        if (p == null) {
            return null;
        } else {
            return new String(p.offset(len-rlen).getBytes(rlen), charset);
        }
    }

    public boolean leadingSpaceAndSurface(String[] leadingSpaceAndSurface)
    {
        if (leadingSpaceAndSurface.length != 2) {
            throw new IllegalArgumentException("leadingSpaceAndSurface.length must be 2.");
        }

        Pointer<Byte> p = _surface();
        int rlen = rlength();
        int len = length();
        int offset = rlen - len;

        if (p == null) {
            return false;
        } else {
            byte[] bytestr = p.offset(-offset).getBytes(rlen);
            leadingSpaceAndSurface[0] = new String(bytestr, 0, offset, charset); // leading space
            leadingSpaceAndSurface[1] = new String(bytestr, offset, len, charset); // surface
            return true;
        }
    }

    @Field(7)
    public Pointer<Byte> _feature()
    {
        return BridJUtil.getPointerField(this, 7);
    }

    public String feature()
    {
        Pointer<Byte> p = _feature();
        if (p == null) {
            return null;
        } else {
            return p.getString(Pointer.StringType.C, charset);
        }
    }

    @Field(8)
    public int _id()
    {
        return this.io.getIntField(this, 8);
    }

    public long id()
    {
        return (_id() & 0xffffffffL);
    }

    @Field(9)
    public short _length()
    {
        return this.io.getShortField(this, 9);
    }

    public int length()
    {
        return (_length() & 0xffff);
    }

    @Field(10)
    public short _rlength()
    {
        return this.io.getShortField(this, 10);
    }

    public int rlength()
    {
        return (_rlength() & 0xffff);
    }

    @Field(11)
    public short _rcAttr()
    {
        return this.io.getShortField(this, 11);
    }

    public int rcAttr()
    {
        return (_rcAttr() & 0xffff);
    }

    @Field(12)
    public short _lcAttr()
    {
        return this.io.getShortField(this, 12);
    }

    public int lcAttr()
    {
        return (_lcAttr() & 0xffff);
    }

    @Field(13)
    public short _posid()
    {
        return this.io.getShortField(this, 13);
    }

    public int posid()
    {
        return (_posid() & 0xffff);
    }

    @Field(14)
    public byte _charType()
    {
        return this.io.getByteField(this, 14);
    }

    public int charType()
    {
        return (_charType() & 0xff);
    }

    @Field(15)
    public byte _stat()
    {
        return this.io.getByteField(this, 15);
    }

    public int stat()
    {
        return (_stat() & 0xff);
    }

    @Field(16)
    public byte _isbest()
    {
        return this.io.getByteField(this, 16);
    }

    public boolean isbest()
    {
        return (_isbest() != 0);
    }

    @Field(17)
    public float alpha()
    {
        return this.io.getFloatField(this, 17);
    }

    @Field(18)
    public float beta()
    {
        return this.io.getFloatField(this, 18);
    }

    @Field(19)
    public float prob()
    {
        return this.io.getFloatField(this, 19);
    }

    @Field(20)
    public short wcost()
    {
        return this.io.getShortField(this, 20);
    }

    @Field(21)
    @CLong
    public long cost()
    {
        return this.io.getCLongField(this, 21);
    }
}
