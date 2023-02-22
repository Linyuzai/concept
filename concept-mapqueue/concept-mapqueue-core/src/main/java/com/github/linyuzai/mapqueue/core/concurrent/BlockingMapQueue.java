package com.github.linyuzai.mapqueue.core.concurrent;

import com.github.linyuzai.mapqueue.core.concept.MapQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public interface BlockingMapQueue<K, V> extends MapQueue<K, V> {

    @Override
    ConcurrentMap<K, V> map();

    @Override
    BlockingQueue<V> queue();
}
