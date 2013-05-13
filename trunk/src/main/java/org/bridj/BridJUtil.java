package org.bridj;

public class BridJUtil
{
    /**
     * DO NOT USE. VERY UGLY HACK.
     */
    @Deprecated
    public static Pointer getPointerField(StructObject obj, int fieldIndex)
    {
        StructIO.FieldDesc fd = obj.io.fields[fieldIndex];
        long peer = obj.peer.getSizeTAtOffset(fd.byteOffset);
        PointerIO io = PointerIO.getInstance(fd.nativeTypeOrPointerTargetType);
        return Pointer.newPointer(io, peer, obj.peer.isOrdered(), -1L, -1L, null, 0L, null, null);
    }
}
