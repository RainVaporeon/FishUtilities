package com.spiritlight.fishutils;

import com.spiritlight.fishutils.misc.arrays.primitive.IntArray;

import java.math.BigDecimal;
import java.util.List;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            IntArray array = IntArray.fromArray(
                    1, 1, 1, 1,
                    2, 2, 2, 2,
                    3, 3, 3, 3,
                    4, 4, 4, 4
            ).toMutable();

            IntArray expected = IntArray.fromArray(
                    1, 2, 3, 4,
                    1, 2, 3, 4,
                    1, 2, 3, 4,
                    1, 2, 3, 4
            );

            IntArray out = array.transpose();

            if(!out.equals(expected)) {
                System.out.printf("""
                        Unexpected transposition result:
                        
                        Expecting %s from %s
                        Got %s
                        """, expected, array, out);
            } else {
                System.out.println("ok");
            }
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