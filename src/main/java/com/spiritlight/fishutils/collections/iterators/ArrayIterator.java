package com.spiritlight.fishutils.collections.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterable<T>, Iterator<T> {
    private static final Object[] EMPTY = new Object[0];
    private final T[] array;
    private int cursor;

    /**
     * Singleton constructor
     */
    private ArrayIterator() {
        this.array = (T[]) EMPTY;
        this.cursor = 0;
    }

    ArrayIterator(T[] array) {
        this.array = array;
        this.cursor = 0;
    }

    public static <T> ArrayIterator<T> create(Object array) {
        if(array == null || !array.getClass().isArray()) {
            return new SingletonIterator<>(array);
        } else {
            return new ArrayIterator<>((T[]) array);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return cursor < array.length;
    }

    @Override
    public T next() {
        return array[cursor++];
    }

    private static class SingletonIterator<E> extends ArrayIterator<E> {
        private final Object element;
        private int cursor;

        private SingletonIterator(Object element) {
            super();
            this.element = element;
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor == 0;
        }

        @Override
        public E next() {
            if(!hasNext()) throw new NoSuchElementException();
            cursor++;
            return (E) this.element;
        }
    }
}
