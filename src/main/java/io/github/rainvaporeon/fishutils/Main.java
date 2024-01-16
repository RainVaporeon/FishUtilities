package io.github.rainvaporeon.fishutils;

import io.github.rainvaporeon.fishutils.utils.secure.SeededGenerator;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            SeededGenerator gen = new SeededGenerator(SeededGenerator.ALPHABET_NUMERICAL);
            gen.setSeed("repos");
            System.out.println(gen.generate(64));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}