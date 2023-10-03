package com.spiritlight.fishutils;

import com.spiritlight.fishutils.random.WeightedRandom;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            WeightedRandom<String> random = new WeightedRandom<>();
            random.addObject("Emmy", 1);
            random.addObject("Desgracia", 49);
            random.addObject("Beta Feesh", 1);
            random.addObject("Angel", 49);
            System.out.println(random.getNext());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}