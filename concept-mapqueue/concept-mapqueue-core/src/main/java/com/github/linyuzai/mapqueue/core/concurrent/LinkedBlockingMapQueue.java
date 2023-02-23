package com.github.linyuzai.mapqueue.core.concurrent;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class LinkedBlockingMapQueue<K, V> extends AbstractBlockingMapQueue<K, V> implements Serializable {

    private static final long serialVersionUID = 1373286646848316162L;

    public LinkedBlockingMapQueue() {
        super(new LinkedHashMap<>());
    }

    public LinkedBlockingMapQueue(int capacity) {
        super(new LinkedHashMap<>(), capacity);
    }
}
