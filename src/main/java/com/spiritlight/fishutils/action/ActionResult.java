package com.spiritlight.fishutils.action;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ActionResult<T> {
    private final Result result;
    private final T returnValue;
    private final Throwable throwable;
    private boolean exceptionHandled;

    public ActionResult(Result result, T returnValue, Throwable throwable) {
        Objects.requireNonNull(result);
        this.result = result;
        this.returnValue = returnValue;
        this.throwable = throwable;
        this.exceptionHandled = throwable == null;
    }

    public ActionResult(Result result, T returnValue) {
        this(result, returnValue, null);
    }

    public ActionResult(Result result, Throwable throwable) {
        this(result, null, throwable);
    }

    /**
     * Accepts this result's returned value
     * @param consumer the consumer to handle the value
     * @return the returned value
     */
    public T accept(Consumer<T> consumer) {
        consumer.accept(this.returnValue);
        return this.returnValue;
    }

    /**
     * Accepts this result and maps it to another value
     * @param function The mapping function
     * @return The action result for the returned value
     * @param <R> The return type
     */
    public <R> ActionResult<R> accept(Function<T, R> function) {
        try {
            return new ActionResult<>(Result.SUCCESS, function.apply(this.returnValue));
        } catch (Throwable t) {
            return new ActionResult<>(Result.ERROR, t);
        }
    }

    /**
     * Handles the exception, if any is present.
     * @param type The type of exception to expect
     * @param action the handler for the exception
     * @return the same ActionResult for chaining purposes
     * @param <X> the type of exception
     * @apiNote if one of the exceptions were handled successfully,
     * then this is considered a "handled result", and so calling
     * {@link ActionResult#getReturnValue()} will not throw the exception.
     */
    public <X extends Throwable> ActionResult<T> expect(Class<X> type, Consumer<X> action) {
        if(this.throwable == null) return this;
        if(type.isAssignableFrom(this.throwable.getClass())) {
            //noinspection unchecked
            action.accept((X) this.throwable);
            this.exceptionHandled = true;
        }
        return this;
    }

    /**
     * Acquires the return value
     * @return the return value
     * @apiNote this method will throw an exception if
     * one is present and is not handled.
     */
    public T getReturnValue() {
        if(!this.exceptionHandled && this.throwable != null) {
            this.throwException();
        }
        return returnValue;
    }

    /**
     * Gets the throwable, if any is present
     * @return the throwable
     * @apiNote if this method is called, it's assumed that
     * the exception is handled, and therefore any future calls of
     * {@link ActionResult#getReturnValue()} will not throw an exception.
     */
    public Throwable getThrowable() {
        this.exceptionHandled = true;
        return throwable;
    }

    /**
     * Handles the return value if this action was successful
     *
     * @param consumer the handler if the result is {@link Result#SUCCESS}
     * @return itself for chaining purposes
     * @see ActionResult#accept(Function)
     * @see ActionResult#accept(Consumer)
     */
    public ActionResult<T> onSuccess(Consumer<T> consumer) {
        if(result == Result.SUCCESS) {
            consumer.accept(this.getReturnValue());
        }
        return this;
    }

    /**
     * Utility method to handle any throwable if the result is not {@link Result#SUCCESS}.
     * @param consumer a consumer that takes a Result and a Throwable for handling
     * @return itself for chaining purposes
     * @see ActionResult#expect(Class, Consumer)
     */
    public ActionResult<T> onFail(BiConsumer<Result, Throwable> consumer) {
        if(result != Result.SUCCESS) {
            consumer.accept(this.result, this.throwable);
            this.exceptionHandled = true;
        }
        return this;
    }

    public boolean isSuccessful() {
        return this.result == Result.SUCCESS;
    }

    public boolean failed() {
        return !isSuccessful();
    }

    public static <T> ActionResult<T> success() {
        return success(null);
    }

    public static <T> ActionResult<T> success(T element) {
        return new ActionResult<>(Result.SUCCESS, element, null);
    }

    public static <T> ActionResult<T> fail() {
        return new ActionResult<>(Result.FAIL, getDefaultException());
    }

    // note: If throwable is not null it used to return FAIL here, if something breaks,
    // this should be the first thing to check out.
    public static <T> ActionResult<T> fail(Throwable throwable) {
        if(throwable == null) return fail();
        return new ActionResult<>(Result.ERROR, throwable);
    }

    public static <T> ActionResult<T> tryAction(Supplier<T> action) {
        try {
            return new ActionResult<>(Result.SUCCESS, action.get());
        } catch (Exception e) {
            return new ActionResult<>(Result.ERROR, e);
        }
    }

    public Result getResult() {
        return result;
    }

    private static Throwable getDefaultException() {
        return new RuntimeException("No information provided").fillInStackTrace();
    }

    private void throwException() {
        if(this.throwable instanceof RuntimeException rte) throw rte;
        if(this.throwable instanceof Error error) throw error;
        throw new RuntimeException(this.throwable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionResult<?> that = (ActionResult<?>) o;
        return exceptionHandled == that.exceptionHandled && result == that.result && Objects.equals(returnValue, that.returnValue) && Objects.equals(throwable, that.throwable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, returnValue, throwable, exceptionHandled);
    }
}
