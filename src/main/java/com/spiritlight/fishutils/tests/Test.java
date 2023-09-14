package com.spiritlight.fishutils.tests;

import com.spiritlight.fishutils.misc.ThrowingRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Represents a test, this class provides multiple useful
 * methods to test for various assertions, as well as running
 * test cases on a certain method.
 */
public class Test<T> implements TestComponent<T> {
    private Object value;
    private final Object target;
    private final Method invocationTarget;
    private String lastMessage;
    private boolean lastSuccess;
    private Throwable lastThrowable;

    public Test(Object target, Class<?> clazz, String method) {
        try {
            this.target = target;
            this.invocationTarget = clazz.getDeclaredMethod(method);
            invocationTarget.setAccessible(true);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("unable to access method", ex);
        }
    }

    public Test(Class<?> clazz, String method) {
        try {
            this.target = clazz.getConstructor().newInstance();
            this.invocationTarget = clazz.getDeclaredMethod(method);
            invocationTarget.setAccessible(true);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("unable to access default constructor or method", ex);
        }
    }

    @Override
    public TestComponent<T> run(Object... args) {
        try {
            this.value = invocationTarget.invoke(target, args);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ex) {
            this.lastThrowable = ex.getCause();
            this.lastSuccess = false;
            this.lastMessage = ex.getCause().getMessage();
        }
        return this;
    }

    @Override
    public TestComponent<T> orElse(ThrowingRunnable action) {
        if(!lastSuccess) action.runUnchecked(t -> new RuntimeException(lastMessage, t));
        return this;
    }

    @Override
    public TestComponent<T> equals(Object other, String... message) {
        if(Objects.equals(this.value, other)) {
            lastSuccess = true;
            return this;
        }
        throw new RuntimeException(message == null ? "not equal" : toString(message));
    }

    @Override
    public TestComponent<T> larger(Comparable<T> other, String... message) {
        if(this.value instanceof Comparable cmp) {
            int val = cmp.compareTo(other);
            if(val > 0) {
                lastSuccess = true;
                return this;
            }
        }
        throw new RuntimeException(message == null ? "not larger" : toString(message));
    }

    @Override
    public TestComponent<T> lesser(Comparable<T> other, String... message) {
        if(this.value instanceof Comparable cmp) {
            int val = cmp.compareTo(other);
            if(val < 0) {
                lastSuccess = true;
                return this;
            }
        }
        throw new RuntimeException(message == null ? "not lesser" : toString(message));
    }

    @Override
    public TestComponent<T> shouldThrow(Class<? extends Throwable> clazz, String... message) {
        if(lastThrowable != null) {
            if(clazz.isAssignableFrom(lastThrowable.getClass())) {
                return this;
            } else {
                throw new RuntimeException("Expected " + clazz.getSimpleName() + ", got " + lastThrowable.getClass().getSimpleName());
            }
        }
        throw new RuntimeException("Expected " + clazz.getSimpleName() + ", got nothing");
    }

    private void throwIfPresent() {
        if(lastThrowable != null) throw new RuntimeException("Unhandled exception: ", lastThrowable);
    }

    private String toString(String... content) {
        return String.join(" ", content);
    }
}
