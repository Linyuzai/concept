package com.github.linyuzai.mapqueue.core.concept;

import java.util.*;

public interface MapQueue<K, V> {

    Map<K, V> map();

    Queue<V> queue();

    void addSynchronizer(Synchronizer<K, V> synchronizer);

    void removeSynchronizer(Synchronizer<K, V> synchronizer);

    interface Synchronizer<K, V> {

        default void beforeEnqueue(K key, V value, Map<K, V> readOnly) {

        }

        default void afterEnqueue(K key, V value, Map<K, V> readOnly) {

        }

        default void beforeDequeue(K key, V value, Map<K, V> readOnly) {

        }

        default void afterDequeue(K key, V value, Map<K, V> readOnly) {

        }
    }
}
