package com.spiritlight.fishutils.misc.arrays.primitive;

import com.spiritlight.fishutils.misc.arrays.PrimitiveArrayLike;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.StreamSupport;

/**
 * A class representing an optionally mutable long array.
 */
public class LongArray extends PrimitiveArrayLike<Long> {
    private final long[] array;
    private final boolean mutable;

    public LongArray() {
        super(long.class, Long.class);
        this.array = new long[0];
        this.mutable = false;
    }

    public LongArray(long[] array) {
        super(long.class, Long.class);
        this.array = array;
        this.mutable = false;
    }

    public LongArray(long[] array, boolean mutable) {
        super(long.class, Long.class);
        this.array = array;
        this.mutable = mutable;
    }

    @Override
    public Long get(int index) {
        checkRange(index);
        return array[index];
    }

    @Override
    public long getAsLong(int index) {
        checkRange(index);
        return array[index];
    }

    @Override
    public void set(int index, Long value) {
        setLong(index, value);
    }

    @Override
    public void setLong(int index, long value) {
        if(mutable) {
            checkRange(index);
            array[index] = value;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public long[] toLongArray() {
        return StreamSupport.stream(this.spliterator(), false).mapToLong(Number::longValue).toArray();
    }

    protected void checkRange(int val) {
        if(val < 0 || val >= size()) throw new IndexOutOfBoundsException(val);
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    public LongArray toMutable() {
        if(this.mutable) return this;
        return new LongArray(this.array.clone(), true);
    }

    public LongArray toImmutable() {
        if(!this.mutable) return this;
        return new LongArray(this.array.clone(), false);
    }

    @Override
    public int size() {
        return array.length;
    }

    /*
        Static collections
     */

    public static final LongUnaryOperator NON_NEGATIVE = i -> Math.max(0, i);

    public static final LongUnaryOperator REVERSE = i -> -i;

    /**
     * Converts the given int array to an immutable LongArray
     * @param array the array
     * @return the wrapped LongArray
     */
    public static LongArray fromArray(long... array) {
        return new LongArray(array);
    }

    /**
     * Creates an immutable array with size {@code size} and all elements set to 0.
     * @param size the size
     * @return an immutable array filled with zeroes.
     */
    public static LongArray createEmpty(int size) {
        return new DefaultLongArray(size, 0);
    }

    /**
     * Creates an immutable array with size {@code size} and all elements set to {@code value}.
     * @param size the size
     * @param value the value
     * @return an immutable array filled with {@code value}.
     */
    public static LongArray create(int size, long value) {
        return new DefaultLongArray(size, value);
    }

    /**
     * Creates an immutable array with size {@code size} and mapped
     * individually to its position as described by {@code mapper}
     * @param size the size
     * @param mapper the mapper
     * @return a new LongArray
     */
    public static LongArray create(int size, IntFunction<Long> mapper) {
        long[] v = new long[size];
        for(int i = 0; i < size; i++) {
            v[i] = mapper.apply(i);
        }
        return new LongArray(v);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        LongArray arr = (LongArray) object;
        return mutable == arr.mutable && Arrays.equals(array, arr.array);
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

    private static class DefaultLongArray extends LongArray {
        private final int size;
        private final long value;

        private DefaultLongArray(int size, long value) {
            super(null, false);
            if(size < 0) throw new IllegalArgumentException("size cannot be negative");
            this.size = size;
            this.value = value;
        }

        @Override
        public Long get(int index) {
            checkRange(index);
            return value;
        }

        @Override
        public long getAsLong(int index) {
            checkRange(index);
            return value;
        }

        @Override
        protected void checkRange(int val) {
            if(val < 0 || val >= size) throw new IndexOutOfBoundsException(val);
        }
    }
}