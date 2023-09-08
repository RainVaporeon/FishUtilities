package com.spiritlight.fishutils.collections.iterators;

import com.spiritlight.fishutils.collections.DefaultedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MultiArrayIterator<T> implements MultiDimensionIterator<T> {
    private final Object array;
    private final Map<Integer, Integer> visits;
    private T[] current;
    private int currentDimension;
    private int cursor = 0;
    private final int dimensions;

    public MultiArrayIterator(Object[] array) {
        if(!this.isArray(Objects.requireNonNull(array))) {
            throw new IllegalArgumentException("not an array");
        }
        this.array = array;
        this.current = (T[]) array;
        this.dimensions = this.determineArrayDimensions();
        this.currentDimension = 0;
        this.visits = new DefaultedMap<>(0);
    }

    public ArrayIterator<T> getIterator() {
        return new ArrayIterator<>(current);
    }

    private int determineArrayDimensions() {
        int size = 0;
        Object[] curr = (Object[]) array;
        while (this.isArray(curr)) {
            int len = curr.length;
            if(curr.length == 0) return size;
            curr = (Object[]) curr[len - 1];
            size++;
        }
        return size;
    }

    private boolean isArray(Object object) {
        return object != null && object.getClass().isArray();
    }

    @Override
    public boolean hasNextDimension() {
        return currentDimension < dimensions;
    }

    @Override
    public T[] nextDimension() {
        if(!hasNextDimension()) throw new NoSuchElementException();
        current = (T[]) ((T[]) array)[currentDimension++];
        cursor = 0;
        return current;
    }

    public boolean hasNextElement() {
        return cursor != current.length;
    }

    @Override
    public boolean hasNext() {
        return hasNextElement() || hasNextDimension();
    }

    @Override
    public T next() {
        if(!hasNextElement()) {
            if(hasNextDimension()) {
                this.nextDimension();
            } else {
                // End of array

            }
        }
        // If end of this array has been reached
        return current[cursor++];
    }
}
