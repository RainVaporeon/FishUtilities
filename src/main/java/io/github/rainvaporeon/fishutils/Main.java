package io.github.rainvaporeon.fishutils;

import io.github.rainvaporeon.fishutils.utils.secure.SeededGenerator;
/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            int value = calculate(15);
            System.out.println(value);
//            String[] sequence = ("""
//                    warp""").split("\n");
//
//            for (String seq : sequence) generateAndPrintCode(seq);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static int calculate(int x) {
        if (x > 10) {
            return x + calculate(x - 1);
        } else if (x > 0) {
            System.out.println("op = " + x + " - " + calculate(x - 1));
            return x - calculate(--x);
        } else {
            return 0;
        }
    }

    private static void generateAndPrintCode(String user) {
        SeededGenerator gen = new SeededGenerator(SeededGenerator.ALPHABET_NUMERICAL + "$/*.#@$%^&");
        gen.setSeed(Main.calculateUserSeed(user));
        gen.addNoise(Main.calculateUserNoise(user));
        System.out.println(user + "'s judge passphrase is: `" + gen.generate(72) + "`.");
    }

    private static String calculateUserSeed(String user) {
        StringBuilder builder = new StringBuilder();
        user.chars().forEach(i -> builder.append(Bits.not(i)));
        return builder.toString();
    }

    private static String calculateUserNoise(String user) {
        return user + "_hero@festival.contest.feesh-ti.me";
    }

    private static class Bits {
        private static int not(int i) {
            return ~i;
        }
    }
}