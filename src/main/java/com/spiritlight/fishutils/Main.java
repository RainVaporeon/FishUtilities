package com.spiritlight.fishutils;

import com.spiritlight.fishutils.tests.Test;
import sun.misc.Unsafe;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        try {

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

    }

    private static <T> Consumer<T> getConsumer(Class<T> clazz) {
        return t -> System.out.println("Received " + clazz.getSimpleName() + " effect");
    }

}