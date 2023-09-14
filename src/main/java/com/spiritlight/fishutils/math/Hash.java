package com.spiritlight.fishutils.math;

import java.util.Objects;

public class Hash {
    public static int forObject(Object... objects) {
        return Objects.hash(objects);
    }
}
