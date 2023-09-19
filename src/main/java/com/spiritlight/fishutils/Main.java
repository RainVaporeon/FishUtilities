package com.spiritlight.fishutils;

import com.spiritlight.fishutils.misc.functions.Predicates;
import com.spiritlight.fishutils.random.WeightedRandom;
import com.spiritlight.fishutils.utils.SpliteratorSupport;
import com.spiritlight.fishutils.utils.secure.SeededGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Main {
    interface Callable {
        boolean get();
    }

    private static Callable retrieve(boolean value) {
        return () -> value;
    }

    public static void main(String[] args) {

        // 1M calls of gathered together: 10026 (0.10026%)
        // 1M calls of split up: 10063 (0.10063%)
        // Difference is minimal and several iterations can minify the odds
        try {
            WeightedRandom<Callable> callers = new WeightedRandom<>();
            WeightedRandom<Boolean> generator = new WeightedRandom<>();
            int count = 0;
            generator.addObject(false, 9900);
            generator.addObject(true, 100);
            for(int i = 0; i < 10000; i++) {
                if(count >= 100) {
                    callers.addObject(retrieve(false), 1);
                } else {
                    boolean val = generator.getNext();
                    if(val) {
                        callers.addObject(retrieve(true), 1);
                        count++;
                    } else {
                        callers.addObject(retrieve(false), 1);
                    }
                }
            }
            System.out.println("Size count: " + callers.getOutcomes().size());
            long winners = callers.infinitelySupplyingStream()
                    .limit(1000000)
                    .parallel()
                    .map(Callable::get)
                    .filter(Predicates.IDENTITY)
                    .count();
            System.out.println(winners);
        } catch (Throwable t) {
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
        }
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