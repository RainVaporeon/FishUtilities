package com.spiritlight.fishutils.action;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A completable ActionResult future.
 * @param <V> the type of the return value
 * @param <T> the future type
 * @apiNote this object provides results that may be incompatible
 */
public class CompletableActionFuture<V, T extends CompletableFuture<V>> implements Action<V> {
    private final T future;
    private V value;
    private Throwable throwable;
    private boolean exceptionHandled;
    public static final Object UNKNOWN_STATE = new Object();

    public CompletableActionFuture(T future) {
        this.future = future;
        this.value = (V) UNKNOWN_STATE;
        setup();
    }

    public CompletableActionFuture<V, T> onComplete(Consumer<V> handler) {
        future.thenAccept(handler);
        return this;
    }

    public CompletableActionFuture<V, T> onException(Function<Throwable, V> handler) {
        future.exceptionally(handler);
        return this;
    }

    private void setup() {
        future.thenAccept(v -> value = v);
        future.exceptionally(t -> {
            this.throwable = t;
            return null;
        });
    }

    @Override
    public V getReturnValue() {
        if(this.throwable != null && !exceptionHandled) throw new RuntimeException(throwable);
        return value;
    }

    public V getReturnValue(V absentValue) {
        if(this.value == UNKNOWN_STATE) return absentValue;
        return this.value;
    }

    public boolean isCompleted() {
        return this.value != UNKNOWN_STATE;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public boolean isSuccessful() {
        return throwable != null && value != UNKNOWN_STATE;
    }
}
