package com.github.linyuzai.download.core.logger;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadCompleteEvent;
import com.github.linyuzai.download.core.event.DownloadStartEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 时间计算日志。
 */
public class TimeSpentCalculationLogger extends LoggingDownloadEventListener {

    /**
     * {@link StopWatch} 缓存
     */
    private final Map<String, StopWatch> stopWatchMap = new ConcurrentHashMap<>();

    /**
     * {@link DownloadContext} 初始化时开始计算，
     * {@link DownloadContext} 销毁时计算事件并移除缓存。
     *
     * @param event 事件
     */
    @Override
    public void logOnEvent(Object event) {
        if (event instanceof DownloadStartEvent) {
            StopWatch watch = new StopWatch();
            watch.start();
            String logId = UUID.randomUUID().toString();
            ((DownloadStartEvent) event).getContext().set(LOG_ID, logId);
            stopWatchMap.put(logId, watch);
        } else if (event instanceof DownloadCompleteEvent) {
            DownloadContext context = ((DownloadCompleteEvent) event).getContext();
            String id = context.get(LOG_ID);
            StopWatch watch = stopWatchMap.remove(id);
            if (watch != null) {
                watch.stop();
                double seconds = watch.getTotalTimeSeconds();
                log(context, "Time spent " + seconds + " s");
            }
        }
    }

    public static class StopWatch {

        private long start;

        private long span;

        public void start() {
            start = System.nanoTime();
        }

        public void stop() {
            span = System.nanoTime() - start;
        }

        public double getTotalTimeSeconds() {
            return span / 1000000000.0;
        }
    }
}
