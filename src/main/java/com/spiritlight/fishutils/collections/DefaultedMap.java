package com.spiritlight.fishutils.collections;

import java.util.HashMap;

public class DefaultedMap<K, V> extends HashMap<K, V> {
    private final V DEFAULT_VALUE;

    public DefaultedMap(V defaultValue) {
        this.DEFAULT_VALUE = defaultValue;
    }

    public V getDefault() {
        return DEFAULT_VALUE;
    }

    @Override
    public V get(Object key) {
        V v = super.get(key);
        return v == null ? DEFAULT_VALUE : v;
    }
}
