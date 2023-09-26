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
            EventBus.INSTANCE.subscribe(Main.class);
            EventBus.INSTANCE.fire(new Event() {
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}