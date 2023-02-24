package com.github.linyuzai.concept.sample.mapqueue;

import com.github.linyuzai.mapqueue.core.concurrent.AbstractBlockingMapQueueTemp;

import java.util.LinkedHashMap;

public class LinkedBlockingMapQueue<K,V> extends AbstractBlockingMapQueueTemp<K,V> {

    public LinkedBlockingMapQueue() {
        super(new LinkedHashMap<>());
    }

    @Override
    public void addSynchronizer(Synchronizer<K, V> synchronizer) {

    }

    @Override
    public void removeSynchronizer(Synchronizer<K, V> synchronizer) {

    }
}
