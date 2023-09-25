package com.spiritlight.fishutils;

import com.spiritlight.fishutils.utils.eventbus.EventBus;
import com.spiritlight.fishutils.utils.eventbus.events.Event;
import com.spiritlight.fishutils.utils.eventbus.events.EventBusSubscriber;
import com.spiritlight.fishutils.utils.secure.SeededGenerator;

import java.util.function.Consumer;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            EventBus a = new EventBus();
            EventBus b = new EventBus();
            EventBus c = new EventBus();
            EventBus d = new EventBus();
            a.inherit(b);
            c.inherit(b);
            d.inherit(a);
            d.inherit(c);
            Object listener = new Object() {
              @EventBusSubscriber public void listen(Event ev) {
                  System.out.println("heard " + ev.getClass().getCanonicalName());
              }
            };
            //a.subscribe(listener);
            //b.subscribe(listener);
            //c.subscribe(listener);
            d.subscribe(listener);
            b.fire(new Event() {
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
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