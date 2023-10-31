package com.spiritlight.fishutils.misc.arrays;

import java.util.function.IntUnaryOperator;
import java.util.stream.StreamSupport;

/**
 * A class representing an optionally mutable integer array.
 */
public class IntArray extends ArrayLike<Integer> {
    private final int[] array;
    private final boolean mutable;

    public IntArray() {
        this.array = new int[0];
        this.mutable = false;
    }

    public IntArray(int[] array) {
        this.array = array;
        this.mutable = false;
    }

    public IntArray(int[] array, boolean mutable) {
        this.array = array;
        this.mutable = mutable;
    }

    @Override
    public Integer get(int index) {
        checkRange(index);
        return array[index];
    }

    public int getAsInt(int index) {
        checkRange(index);
        return array[index];
    }

    @Override
    public void set(int index, Integer value) {
        setInt(index, value);
    }

    public void setInt(int index, int value) {
        if(mutable) {
            checkRange(index);
            array[index] = value;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public int[] toIntArray() {
        return StreamSupport.stream(this.spliterator(), false).mapToInt(Integer::intValue).toArray();
    }

    protected void checkRange(int val) {
        if(val < 0 || val >= size()) throw new IndexOutOfBoundsException(val);
    }

    public IntArray toMutable() {
        return new IntArray(this.array.clone(), true);
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public int size() {
        return array.length;
    }

    /*
        Static collections
     */

    public static final IntUnaryOperator NON_NEGATIVE = i -> Math.max(0, i);

    public static final IntUnaryOperator REVERSE = i -> -i;

    /**
     * Converts the given int array to an immutable IntArray
     * @param array the array
     * @return the wrapped IntArray
     */
    public static IntArray fromArray(int... array) {
        return new IntArray(array);
    }

    /**
     * Creates an immutable array with size {@code size} and all elements set to 0.
     * @param size the size
     * @return an immutable array filled with zeroes.
     */
    public static IntArray createEmpty(int size) {
        return new DefaultIntArray(size, 0);
    }

    /**
     * Creates an immutable array with size {@code size} and all elements set to {@code value}.
     * @param size the size
     * @param value the value
     * @return an immutable array filled with {@code  value}.
     */
    public static IntArray create(int size, int value) {
        return new DefaultIntArray(size, value);
    }

    /**
     * Creates an immutable array with size {@code size} and mapped
     * individually to its position as described by {@code mapper}
     * @param size the size
     * @param mapper the mapper
     * @return a new IntArray
     */
    public static IntArray create(int size, IntUnaryOperator mapper) {
        int[] v = new int[size];
        for(int i = 0; i < size; i++) {
            v[i] = mapper.applyAsInt(i);
        }
        return new IntArray(v);
    }

    private static class DefaultIntArray extends IntArray {
        private final int size;
        private final int value;

        private DefaultIntArray(int size, int value) {
            super(null, false);
            if(size < 0) throw new IllegalArgumentException("size cannot be negative");
            this.size = size;
            this.value = value;
        }

        @Override
        public Integer get(int index) {
            checkRange(index);
            return value;
        }

        @Override
        public int getAsInt(int index) {
            checkRange(index);
            return value;
        }

        @Override
        protected void checkRange(int val) {
            if(val < 0 || val >= size) throw new IndexOutOfBoundsException(val);
        }
    }
}
