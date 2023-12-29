package com.spiritlight.fishutils;

import com.spiritlight.fishutils.utils.secure.SeededGenerator;

/**
 * where i test dumb stuffs
 */
public class Main {

    public static void main(String[] args) {
        try {
            String[] color = {"black", "white"};
            String[] piece = {"pawn", "knight", "bishop", "rook", "queen", "king"};
            for(String clr : color) {
                for(String pie: piece) {
                    System.out.println(clr.toUpperCase() + "_" + pie.toUpperCase() + " = helper.get(\"" +
                            clr + "-" + pie + "\");");
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}