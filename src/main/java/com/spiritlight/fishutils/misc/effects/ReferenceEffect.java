package com.spiritlight.fishutils.misc.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A reactive effect.
 * @param <T>
 */
public class ReferenceEffect<T> implements Effect<T> {
    protected final List<Consumer<T>> listeners = new ArrayList<>();
    protected T value;

    ReferenceEffect(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        this.value = value;
        this.onUpdate(value);
    }

    @Override
    public boolean addListener(Consumer<T> listener) {
        return listeners.add(listener);
    }

    @Override
    public boolean removeListener(Consumer<T> consumer) {
        return listeners.remove(consumer);
    }

    @Override
    public void onUpdate(T value) {
        for(Consumer<T> listener : this.listeners) {
            listener.accept(value);
        }
    }
}
