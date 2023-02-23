package com.github.linyuzai.concept.sample.mapqueue;

import com.github.linyuzai.mapqueue.core.concept.MapQueue;
import com.github.linyuzai.mapqueue.core.concurrent.BlockingMapQueue;
import com.github.linyuzai.mapqueue.core.concurrent.LinkedBlockingMapQueue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class MapQueueSample {

    private final Map<String, String> map;

    private final BlockingQueue<String> queue;

    public MapQueueSample() {
        BlockingMapQueue<String, String> bmq = new LinkedBlockingMapQueue<>();
        bmq.addSynchronizer(new MapQueue.Synchronizer<String, String>() {

            @Override
            public void afterEnqueue(String key, String value, Map<String, String> readOnly) {
                log.info("Put: " + value + ", Current: " + readOnly.toString());
            }

            @Override
            public void afterDequeue(String key, String value, Map<String, String> readOnly) {
                log.info("Take: " + value + ", Current: " + readOnly.toString());
            }
        });

        this.map = bmq.map();
        this.queue = bmq.queue();
    }

    public void start() {
        startPut("A");
        startPut("B");
        startPut("C");
        startPut("D");
        startTake();
    }

    public void startPut(String s) {
        Thread thread = new Thread() {

            int i;

            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    String v = s + i++;
                    map.put(s, v);
                    Thread.sleep(500);
                }
            }
        };
        thread.setName("Put" + s);
        thread.start();
    }

    @SneakyThrows
    public void startTake() {
        while (true) {
            String s = queue.take();
            Thread.sleep(1200);
        }
    }
}
