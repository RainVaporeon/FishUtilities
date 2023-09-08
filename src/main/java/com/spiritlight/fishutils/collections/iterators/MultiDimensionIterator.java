package com.spiritlight.fishutils.collections.iterators;

import java.util.Iterator;

public interface MultiDimensionIterator<T> extends Iterator<T>, Iterable<T> {
    /**
     * Checks whether there are any more dimensions available.
     * @return true if {@link MultiDimensionIterator#nextDimension()}
     * will not throw an exception.
     */
    boolean hasNextDimension();

    /**
     * Advances this iterator to the next dimension and returns the
     * advanced dimension, resetting the cursor in the process if
     * present.
     * @return the next dimension
     * @throws java.util.NoSuchElementException if there are no next dimension
     */
    T[] nextDimension();

    boolean hasNext();

    T next();

    @Override
    default Iterator<T> iterator() {
        return this;
    }
}
