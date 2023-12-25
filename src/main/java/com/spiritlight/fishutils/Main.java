package com.spiritlight.fishutils;

import com.spiritlight.fishutils.utils.secure.SeededGenerator;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            SeededGenerator gen = new SeededGenerator(SeededGenerator.ALPHABET_NUMERICAL);
            gen.setSeed("web");
            gen.addNoise("web");
            System.out.println(gen.generate(256));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}