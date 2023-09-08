package com.spiritlight.fishutils.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * A reactive effect.
 * @param <T>
 */
public class Effect<T> {
    protected List<Listener> listeners = new ArrayList<>();
    protected T value;

    private Effect(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        for(Listener listener : this.getRegisteredListeners()) {
            listener.onUpdate(value);
        }
    }

    public void addListener(Consumer<T> listener) {
        listeners.add(new Listener() {
            @Override
            void onUpdate(T t) {
                listener.accept(t);
            }
        });
    }

    protected Collection<Listener> getRegisteredListeners() {
        return listeners;
    }

    public static <T> Effect<T> of(T obj) {
        return new Effect<>(obj);
    }

    protected abstract class Listener {
        abstract void onUpdate(T t);
    }
}
