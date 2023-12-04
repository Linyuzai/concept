package com.github.linyuzai.download.core.logger;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadCompletedEvent;
import com.github.linyuzai.download.core.event.DownloadStartedEvent;

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
        if (event instanceof DownloadStartedEvent) {
            StopWatch watch = new StopWatch();
            watch.start();
            DownloadContext context = ((DownloadStartedEvent) event).getContext();
            stopWatchMap.put(getLogId(context), watch);
        } else if (event instanceof DownloadCompletedEvent) {
            DownloadContext context = ((DownloadCompletedEvent) event).getContext();
            String logId = getLogId(context);
            StopWatch watch = stopWatchMap.remove(logId);
            if (watch != null) {
                watch.stop();
                double seconds = watch.seconds();
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

        public double seconds() {
            return span / 1000000000.0;
        }
    }
}
