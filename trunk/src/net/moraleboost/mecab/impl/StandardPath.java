package net.moraleboost.mecab.impl;

import net.moraleboost.mecab.Path;
import org.bridj.BridJUtil;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;

import java.nio.charset.Charset;

public class StandardPath extends StructObject implements Path
{
    private Charset charset;

    protected StandardPath(Pointer<StandardPath> p, Charset charset)
    {
        super(p);
        this.charset = charset;
    }

    @Field(0)
    public Pointer<StandardNode> _rnode()
    {
        return BridJUtil.getPointerField(this, 0);
    }

    public StandardNode rnode()
    {
        Pointer<StandardNode> p = _rnode();
        return (p == null) ? null : new StandardNode(p, charset);
    }

    @Field(1)
    public Pointer<StandardPath> _rnext()
    {
        return BridJUtil.getPointerField(this, 1);
    }

    public StandardPath rnext()
    {
        Pointer<StandardPath> p = _rnext();
        return (p == null) ? null : new StandardPath(p, charset);
    }

    @Field(2)
    public Pointer<StandardNode> _lnode()
    {
        return BridJUtil.getPointerField(this, 2);
    }

    public StandardNode lnode()
    {
        Pointer<StandardNode> p = _lnode();
        return (p == null) ? null : new StandardNode(p, charset);
    }

    @Field(3)
    public Pointer<StandardPath> _lnext()
    {
        return BridJUtil.getPointerField(this, 3);
    }

    public StandardPath lnext()
    {
        Pointer<StandardPath> p = _lnext();
        return (p == null) ? null : new StandardPath(p, charset);
    }

    @Field(4)
    public int cost()
    {
        return this.io.getIntField(this, 4);
    }

    @Field(5)
    public float prob()
    {
        return this.io.getFloatField(this, 5);
    }
}
