package com.spiritlight.fishutils.misc.arrays.primitive;

import com.spiritlight.fishutils.misc.arrays.ArrayLike;
import com.spiritlight.fishutils.misc.arrays.PrimitiveArrayLike;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.stream.StreamSupport;

/**
 * A class representing an optionally mutable integer array.
 */
public class IntArray extends PrimitiveArrayLike<Integer> {
    private final int[] array;
    private final boolean mutable;

    public IntArray() {
        super(int.class, Integer.class);
        this.array = new int[0];
        this.mutable = false;
    }

    public IntArray(int[] array) {
        super(int.class, Integer.class);
        this.array = array;
        this.mutable = false;
    }

    public IntArray(int[] array, boolean mutable) {
        super(int.class, Integer.class);
        this.array = array;
        this.mutable = mutable;
    }

    @Override
    public Integer get(int index) {
        checkRange(index);
        return array[index];
    }

    @Override
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
        return this.array.clone();
    }

    protected void checkRange(int val) {
        if(val < 0 || val >= size()) throw new IndexOutOfBoundsException(val);
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    public IntArray toMutable() {
        if(this.mutable) return this;
        return new IntArray(this.array.clone(), true);
    }

    public IntArray toImmutable() {
        if(!this.mutable) return this;
        return new IntArray(this.array.clone(), false);
    }

    public IntArray transpose() {
        if(Math.sqrt(this.size()) != (int) Math.sqrt(size())) throw new IllegalArgumentException("not a square sized array");

        IntArray array = createMutable(this.size());

        int length = (int) Math.sqrt(this.size());

        int idx = 0;
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                int index = i + j * length;
                array.set(idx++, get(index));
            }
        }
        return array;
    }

    @Override
    public IntArray transpose(int row, int col) {
        IntArray array = IntArray.createMutable(row * col);
        int idx = 0;
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                int index = i + j * col;
                array.set(idx++, get(index));
            }
        }
        return array;
    }

    public IntArray copy() {
        return new IntArray(this.array.clone());
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

    public static IntArray createMutable(int size) {
        return new IntArray(new int[size], true);
    }

    public static IntArray createMutable(int size, int defaultElements) {
        int[] a = new int[size];
        Arrays.fill(a, defaultElements);
        return new IntArray(a, true);
    }

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IntArray integers = (IntArray) object;
        return Arrays.equals(array, integers.array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mutable);
        result = 31 * result + Arrays.hashCode(array);
        return result;
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
