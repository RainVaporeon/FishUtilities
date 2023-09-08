package com.spiritlight.fishutils;

import com.spiritlight.fishutils.collections.ArrayUtils;
import com.spiritlight.fishutils.collections.CollectionMisc;
import com.spiritlight.fishutils.collections.CollectionUtils;
import com.spiritlight.fishutils.collections.iterators.MultiArrayIterator;
import com.spiritlight.fishutils.misc.Effect;

import java.util.*;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        try {
            Object[][][] data = new Object[50][50][50];
            MultiArrayIterator<Object> it = new MultiArrayIterator<>(data);
            int size = 0;
            for(Object o : it) size++;
            System.out.println(size); // 125000
            Arrays.deepToString(data);
        } catch (Throwable t) {
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
        }
    }

    private static void populateArray(Object[][][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                for(int k = 0; k < arr[i][j].length; k++) {
                    arr[i][j][k] = new Object();
                }
            }
        }
    }

    private static void effectTest() {
        Effect<Object> first = Effect.of(new Object());
        Effect<Integer> second = Effect.of(12);
        Effect<Double> third = Effect.of(123.4);

        first.addListener(getConsumer(Object.class));
        second.addListener(getConsumer(Integer.class));
        third.addListener(getConsumer(Double.class));

        first.setValue(new Object());
        second.setValue(42);
        third.setValue(69.420);
    }

    private static <T> Consumer<T> getConsumer(Class<T> clazz) {
        return t -> System.out.println("Received " + clazz.getSimpleName() + " effect");
    }

}