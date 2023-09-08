package com.spiritlight.fishutils.assertions;

import java.util.Collection;
import java.util.Objects;

public final class Assert {
    public static void equals(Object o1, Object o2, String... message) {
        if(Objects.equals(o1, o2)) return;
        throw new AssertionError(concat(message));
    }

    public static void contains(Collection<?> coll, String message, Object... o) {
        if(coll == null || coll.stream().noneMatch(element -> Objects.equals(element, o))) {
            throw new AssertionError(message);
        }
    }

    private static String concat(String... message) {
        return String.join(" ", message);
    }
}
