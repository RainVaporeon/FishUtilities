package com.spiritlight.fishutils;

import com.spiritlight.fishutils.objects.ObjectUtils;
import com.spiritlight.fishutils.utils.secure.SeededGenerator;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Main {
    interface Callable {
        boolean get();
    }

    public static void main(String[] args) {

    }

    private static void printElements(Spliterator<?> sp) {
        sp.forEachRemaining(System.out::println);
    }


    private static void printSeed(SeededGenerator gen) {
        System.out.println("SEED=" + gen.getSeed());
        System.out.println("VALUE30=" + gen.generate(30));
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