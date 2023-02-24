package com.github.linyuzai.concept.sample.mapqueue;

import com.github.linyuzai.mapqueue.core.concurrent.BlockingMapQueue;

import java.util.concurrent.ConcurrentMap;

public class MapQueueBugSample {

    private BlockingMapQueue<String, String> bmq = new LinkedBlockingMapQueue<>();

    public void start() {
        ConcurrentMap<String, String> map = bmq.map();
        map.put("A","A1");
        map.put("A","A2");
    }
}
