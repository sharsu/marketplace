package com.inspiware.marketplace.services.store;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PreciousMetalsMapStore<K, V> implements Store<K, V> {

    private final String name;

    private ConcurrentMap<K, V> internalStore;

    public PreciousMetalsMapStore(String name) {
        this.name = name;
        this.internalStore = new ConcurrentHashMap<>(256);
    }

    public final String getName() {
        return this.name;
    }

    @Override
    public V get(K key) {
        return this.internalStore.get(key);
    }

    @Override
    public V get(K key, Predicate<V> predicate) {
        V value = this.internalStore.get(key);
        if (value != null && predicate.test(value)) {
            return value;
        }
        return null;
    }

    @Override
    public Set<V> getAll(Predicate<V> predicate) {
        return internalStore.values().stream().filter(v -> predicate.test(v)).collect(Collectors.toSet());
    }

    @Override
    public void registerOrder(K key, V entity) {
        internalStore.put(key, entity);
    }

    @Override
    public V cancel(K key, Predicate<V> predicate) {
        V value = this.internalStore.get(key);
        if (value != null && predicate.test(value)) {
            return internalStore.remove(key);
        }
        return null;
    }
}
