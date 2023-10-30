package com.spiritlight.fishutils;

import com.spiritlight.fishutils.misc.FieldLookup;
import com.spiritlight.fishutils.utils.secure.SeededGenerator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            SeededGenerator generator = new SeededGenerator(SeededGenerator.ALPHABET_NUMERICAL);
            generator.setSeed("web");
            generator.addNoise("web");
            System.out.println(generator.generate(256));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static double variance(List<BigDecimal> list) {
        System.out.println(list);
        double avg = list.stream().mapToDouble(BigDecimal::doubleValue).average().getAsDouble();
        System.out.println(avg);
        return list.stream().map(bd -> bd.subtract(BigDecimal.valueOf(avg)))
                .map(bd -> bd.pow(2)).mapToDouble(BigDecimal::doubleValue).sum() / (list.size() - 1.0);
    }
}