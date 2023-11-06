package com.spiritlight.fishutils;

import java.math.BigDecimal;
import java.util.List;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {

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