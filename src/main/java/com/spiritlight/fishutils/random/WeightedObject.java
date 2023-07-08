package com.spiritlight.fishutils.random;

public interface WeightedObject {

    int getWeight();

    default Object getObject() {
        return this;
    }
}
