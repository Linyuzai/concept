package com.github.linyuzai.mapqueue.core.concurrent;

import java.util.LinkedHashMap;

public class LinkedBlockingMapQueue<K, V> extends AbstractBlockingMapQueue<K, V> {

    public LinkedBlockingMapQueue() {
        super(new LinkedHashMap<>());
    }

    public LinkedBlockingMapQueue(int capacity) {
        super(new LinkedHashMap<>(), capacity);
    }
}
