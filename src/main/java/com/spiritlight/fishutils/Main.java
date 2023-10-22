package com.spiritlight.fishutils;

import com.spiritlight.fishutils.misc.FieldLookup;
import com.spiritlight.fishutils.utils.secure.SeededGenerator;

import java.util.Locale;
import java.util.Scanner;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            String value = "nice";
            System.out.println(
                    value.replaceAll("[^\\w]", "")
                            .toLowerCase(Locale.ROOT)
                            .chars()
                            .anyMatch(ch -> value.indexOf(ch) != value.lastIndexOf(ch))
            );
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}