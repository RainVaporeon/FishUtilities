package com.spiritlight.fishutils.misc.arrays;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Base class for any primitive array-alike.
 * This class provides support for all primitive getter methods,
 * but does not provide setter methods.
 * @param <T> the type
 */
public abstract class PrimitiveArrayLike<T> extends ArrayLike<T> {
    protected final Class<?> primitiveType;

    public PrimitiveArrayLike(Class<?> primitiveType, Class<T> type) {
        super(type);
        this.primitiveType = primitiveType;
    }

    public boolean getAsBoolean(int index) {
        return (boolean) this.getAt(index);
    }

    public byte getAsByte(int index) {
        return (byte) this.getAt(index);
    }

    public short getAsShort(int index) {
        return (short) this.getAt(index);
    }

    public char getAsCharacter(int index) {
        return (char) this.getAt(index);
    }

    public int getAsInt(int index) {
        return (int) this.getAt(index);
    }

    public float getAsFloat(int index) {
        return (float) this.getAt(index);
    }

    public double getAsDouble(int index) {
        return (double) this.getAt(index);
    }

    public long getAsLong(int index) {
        return (long) this.getAt(index);
    }

    private Object getAt(int index) {
        Iterator<?> it = this.iterator();
        for(int i = 0; it.hasNext(); i++) {
            Object o = it.next();
            if(i == index) return o;
        }
        throw new IndexOutOfBoundsException(index);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj instanceof PrimitiveArrayLike<?> al) {
            if(this.type != al.type && this.primitiveType != al.primitiveType) return false;
            return Arrays.deepEquals(this.toArray(), al.toArray());
        }
        return false;
    }
}
