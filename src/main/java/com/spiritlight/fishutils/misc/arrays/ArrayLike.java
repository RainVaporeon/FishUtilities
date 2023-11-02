package com.spiritlight.fishutils.misc.arrays;

import com.spiritlight.fishutils.misc.annotations.New;

import java.io.Serializable;
import java.util.Iterator;

/**
 * An optionally mutable class representing an array.
 * @param <T> the array type
 */
public abstract class ArrayLike<T> implements Cloneable, Iterable<T>, Serializable {

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

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException error) {
            // Java's fault
            throw new InternalError(error);
        }
    }
}