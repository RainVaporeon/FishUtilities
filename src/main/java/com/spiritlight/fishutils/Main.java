package com.spiritlight.fishutils;

import com.spiritlight.fishutils.random.WeightedRandom;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        WeightedRandom<Integer> random = new WeightedRandom<>();
        random.addObject(1, 1);
        random.addObject(2, 2);
        random.addObject(3, 3);
        random.addObject(4, 4);
        Map<Integer, Integer> iMap = new HashMap<>();
        for(int i = 0; i < 1000000; i++) {
            int val = random.getNext();
            if(iMap.containsKey(val)) {
                iMap.put(val, iMap.get(val) + 1);
            } else {
                iMap.put(val, 1);
            }
        }
        // Expect around 1000 1, 2000 2, 3000 3, 4000 4, if it's close enough this succeeds.
        System.out.println(iMap);
    }
}