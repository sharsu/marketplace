package com.inspiware.marketplace.services.store;

import java.util.Set;
import java.util.function.Predicate;

public interface Store<K, V> {
    V get(K key);

    V get(K key, Predicate<V> predicate);

    Set<V> getAll(Predicate<V> predicate);

    void registerOrder(K key, V value);

    V cancel(K key, Predicate<V> predicate);
}
