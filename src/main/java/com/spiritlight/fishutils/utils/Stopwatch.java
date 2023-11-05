package com.spiritlight.fishutils.utils;

import com.spiritlight.fishutils.collections.DefaultedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A general stopwatch providing basic timer functionalities.
 * @since 1.2.7
 */
public class Stopwatch {
    private long startTime;
    private final Map<String, Long> timeMap = new DefaultedMap<>(0L);

    public Stopwatch() {}

    /**
     * Starts the stopwatch. Clearing all the fenced time in the process.
     */
    public void start() {
        clear();
        startTime = System.nanoTime();
    }

    /**
     * Adds a fence to current stopwatch. This value can be retrieved later on.
     * @param name the fence name
     * @return the previous time elapsed for the fence
     */
    public long fence(String name) {
        Long l = timeMap.put(Objects.requireNonNull(name), System.nanoTime());
        return l == null ? 0 : System.nanoTime() - l;
    }

    /**
     * Retrieves the time elapsed since this fence
     * @param name the fence name
     * @return the time, -1 if it is not present.
     */
    public long get(String name) {
        if(!timeMap.containsKey(name)) return -1L;
        return System.nanoTime() - timeMap.get(name);
    }

    /**
     * Retrieves all time maps, the String being the key, and the value being
     * time elapsed. Null is used as a key here to denote the time since this
     * stopwatch started.
     * @return the time map, and the null key being time elapsed since this
     * watch has started.
     */
    public Map<String, Long> getAll() {
        Map<String, Long> map = new HashMap<>();
        timeMap.forEach((str, time) -> {
            if(time == null) return;
            map.put(str, System.nanoTime() - time);
        });
        map.put(null, System.nanoTime() - startTime);
        return map;
    }

    /**
     * Stops this stopwatch and returns the time elapsed since this watch started
     * @return the time since this watch started
     */
    public long stop() {
        return System.nanoTime() - startTime;
    }

    /**
     * Clears all fence mappings.
     */
    public void clear() {
        timeMap.clear();
    }
}
