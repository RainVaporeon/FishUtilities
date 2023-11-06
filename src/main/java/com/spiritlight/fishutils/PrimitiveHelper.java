package com.spiritlight.fishutils;

import com.spiritlight.fishutils.misc.arrays.ArrayLike;

import java.util.Arrays;

public class PrimitiveHelper {

    private static final ArrayLike<String> PRIMITIVES = ArrayLike.of(
            "byte", "short", "char", "int", "float", "double", "long", "boolean"
    );

    /**
     * Replaces each occurrence of {} into the primitive type.
     * If uppercase on first character is preferred, use {A} instead.
     * @param form the string form
     * @param exclusions the exclusions, case-sensitive.
     * @return a string with {} replaced with primitive types
     */
    public static String generate(String form, String... exclusions) {
        StringBuilder builder = new StringBuilder();
        for(String type : PRIMITIVES) {
            if(Arrays.asList(exclusions).contains(type)) continue;
            builder.append(form.replace("{}", type).replace("{A}", Character.toUpperCase(type.charAt(0)) + type.substring(1))).append("\n");

        }
        return builder.toString();
    }
}
