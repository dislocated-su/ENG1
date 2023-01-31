package com.devcharles.piazzapanic.utility;
/**
 * Create a generic tuple.
 */
public class Pair<K, V> {
    public K first;
    public V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }
}
