package com.github.linyuzai.mapqueue.core.concurrent;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedBlockingMapQueue<K, V> extends AbstractBlockingMapQueue<K, V> implements Serializable {

    private static final long serialVersionUID = 2226674414226776978L;

    public LinkedBlockingMapQueue() {
    }

    public LinkedBlockingMapQueue(int capacity) {
        super(capacity);
    }

    public LinkedBlockingMapQueue(boolean fair) {
        super(fair);
    }

    public LinkedBlockingMapQueue(Map<K, V> map) {
        super(map);
    }

    public LinkedBlockingMapQueue(int capacity, Map<K, V> map) {
        super(capacity, map);
    }

    public LinkedBlockingMapQueue(boolean fair, Map<K, V> map) {
        super(fair, map);
    }

    public LinkedBlockingMapQueue(int capacity, boolean fair) {
        super(capacity, fair);
    }

    public LinkedBlockingMapQueue(int capacity, boolean fair, Map<? extends K, ? extends V> map) {
        super(capacity, fair, map);
    }

    @Override
    protected Map<K, V> createMap() {
        return new LinkedHashMap<>();
    }
}
