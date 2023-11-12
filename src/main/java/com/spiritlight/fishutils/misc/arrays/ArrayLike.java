package com.spiritlight.fishutils.misc.arrays;

import com.spiritlight.fishutils.misc.annotations.New;
import com.spiritlight.fishutils.misc.arrays.primitive.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * An optionally mutable class representing an array.
 * @param <T> the array type
 */
public abstract class ArrayLike<T> implements Cloneable, Iterable<T>, Serializable {

    /**
     * The representing array type.
     * @since 1.2.7
     */
    protected final Class<?> type;

    public ArrayLike(Class<T> type) {
        this.type = type;
    }

    /**
     * Retrieves the element at this index
     * @param index the index
     * @return the element
     */
    public abstract T get(int index);

    /**
     * Sets the index to this element
     * @param index the index
     * @param value the value
     * @apiNote This method throws an exception if {@link ArrayLike#isMutable()}
     * returns {@code false}
     */
    public abstract void set(int index, T value);

    /**
     * Retrieves the array size
     * @return the array size
     */
    public abstract int size();

    /**
     * Checks whether the array is mutable. If not, {@link ArrayLike#set(int, Object)}
     * will throw an exception.
     * @return whether this array is mutable
     */
    public abstract boolean isMutable();

    /**
     * Checks the range and throws an {@link IndexOutOfBoundsException} if
     * {@code value} is less than 0 or larger or equal to {@link ArrayLike#size()}
     * @param value the value
     */
    protected void checkRange(int value) {
        if(value < 0 || value >= size()) throw new IndexOutOfBoundsException(value);
    }

    /**
     * Fills this array in a given range to the specified value
     * @param from the offset to start, inclusive
     * @param to the offset to end, exclusive
     * @param value the value
     */
    public void fill(int from, int to, T value) {
        for(int i = from; i < to; i++) {
            set(i, value);
        }
    }

    /**
     * Fills this array in a given range to the specified value
     * @param from the offset to start, inclusive
     * @param to the offset to end, exclusive
     * @param mapper the value mapper, the actual position is
     *               supplied into this mapper
     */
    public void fill(int from, int to, IntFunction<T> mapper) {
        for(int i = from; i < to; i++) {
            set(i, mapper.apply(i));
        }
    }

    /**
     * Converts the representing array into an object array
     * @return the array, may or may not represent itself and
     * makes no guarantee that the returned element cannot be mutated
     * or be reflected onto the array.
     * @apiNote since this method makes no guarantee that the returned
     * array is a copy of itself or a reference to its backing array,
     * nor does it guarantee this is deep-copied, it is highly advised
     * against modification of the returned array.
     */
    public @New T[] toArray() {
        Object[] ret = new Object[this.size()];
        for (int i = 0; i < size(); i++) {
            ret[i] = get(i);
        }
        return (T[]) ret;
    }

    @Override
    public @New Iterator<T> iterator() {
        return new ArrayIterator<>(this);
    }


    /**
     * Clones this array type.
     * @return the clone of this array
     * @apiNote As this class wraps a mutable array, it is
     * almost always recommended to
     */
    @Override @SuppressWarnings("unchecked")
    public ArrayLike<T> clone() {
        try {
            return (ArrayLike<T>) super.clone();
        } catch (CloneNotSupportedException error) {
            // Java's fault
            throw new InternalError(error);
        } catch (ClassCastException ex) {
            // Again, Java's fault
            throw new InternalError(ex);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(this.toArray());
    }

    /**
     * Checks for object equality between one and another.
     * @param obj the object
     * @return whether both backing arrays are equal
     * @implNote it is almost always recommended that the implementation
     * should implement their own equals method, as this method will
     * fetch both side's array via the {@link ArrayLike#toArray()} method,
     * which can be slow at times, thus nearly any implementation will be
     * beneficial.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj instanceof ArrayLike<?> al) {
            if(this.type != al.type) return false;
            return Arrays.deepEquals(this.toArray(), al.toArray());
        }
        return false;
    }

    @SafeVarargs
    public static <T> ArrayLike<T> of(T... elements) {
        return new ReferenceArray<>(elements);
    }

    public static IntArray of(int... elements) {
        return new IntArray(elements);
    }

    public static DoubleArray of(double... elements) {
        return new DoubleArray(elements);
    }

    public static ShortArray of(short... elements) {
        return new ShortArray(elements);
    }

    public static LongArray of(long... elements) {
        return new LongArray(elements);
    }

    public static ByteArray of(byte... el) {
        return new ByteArray(el);
    }

    public static FloatArray of(float... f) {
        return new FloatArray(f);
    }

    public static CharacterArray of(char... c) {
        return new CharacterArray(c);
    }

    public static BooleanArray of(boolean... boo) {
        return new BooleanArray(boo);
    }
}
