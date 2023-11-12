package com.spiritlight.fishutils.misc;

import com.spiritlight.fishutils.misc.annotations.DelegatesToShadow;

import java.util.Objects;

/**
 * A stable field that updates at most once during the program runtime.
 * @param <T> the type
 * @since 1.2.6
 */
public class StableField<T> {
    @DelegatesToShadow.Target
    private T t;
    private boolean modified;

    /**
     * Creates a stable field with the default value as {@code null}
     */
    public StableField() {
        this(null);
    }

    /**
     * Creates a stable field with the default value as
     * the value provided.
     * @param t the value
     */
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

    /**
     * Checks whether the two objects are equal.
     * This may return true if the underlying object's
     * {@code equals} method returns true.
     * @param object the object to compare to
     * @return true if this object's underlying object is identical
     * to the other, or if both stable fields hold the same object
     */
    @Override @DelegatesToShadow
    public boolean equals(Object object) {
        if(Objects.equals(t, object)) return true;
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        StableField<?> that = (StableField<?>) object;
        return Objects.equals(t, that.t);
    }

    @Override @DelegatesToShadow
    public int hashCode() {
        return Objects.hashCode(t);
    }

    @Override @DelegatesToShadow
    public String toString() {
        return String.valueOf(t);
    }
}
