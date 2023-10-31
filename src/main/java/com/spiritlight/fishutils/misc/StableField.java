package com.spiritlight.fishutils.misc;

/**
 * A stable field that updates at most once during the program runtime.
 * @param <T> the type
 * @since 1.2.6
 */
public class StableField<T> {
    private T t;
    private boolean modified;

    public StableField(T t) {
        this.t = t;
        this.modified = false;
    }

    /**
     * Sets this stable field to a new value
     * @param t the value
     * @throws IllegalStateException if it was previously modified
     */
    public void set(T t) {
        if(modified) throw new IllegalStateException("stable field modified more than once");
        this.modified = true;
        this.t = t;
    }

    /**
     * Gets the value
     * @return the value
     */
    public T get() {
        return t;
    }

    /**
     * Checks whether this field was modified before. If yes,
     * subsequent calls to {@link StableField#set(Object)} will throw
     * an exception.
     * @return whether this field was modified before
     */
    public boolean isModified() {
        return modified;
    }
}
