package com.spiritlight.fishutils;

import com.spiritlight.fishutils.collections.CollectionUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> i1 = List.of(1, 2, 3, 4, 5);
        List<Integer> i2 = List.of(6, 7, 8, 9, 0);
        List<Integer> i3 = List.of(-1, -2, -3, -4);
        List<Integer> i4 = List.of(-5, -6, -7, -8);

        List<List<Integer>> all = List.of(i1, i2, i3, i4);

        System.out.println(all.stream().collect(CollectionUtils.extractToList()));
    }
}