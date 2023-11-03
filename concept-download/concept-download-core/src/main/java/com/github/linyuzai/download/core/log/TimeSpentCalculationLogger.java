package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadCompletedEvent;
import com.github.linyuzai.download.core.event.DownloadStartedEvent;

import java.util.Map;
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
            stopWatchMap.put(((DownloadStartedEvent) event).getContext().get(LOG_ID), watch);
        } else if (event instanceof DownloadCompletedEvent) {
            DownloadContext context = ((DownloadCompletedEvent) event).getContext();
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

        public void start() {

        }

        public void stop() {

        }

        public double getTotalTimeSeconds() {
            return 0.0;
        }
    }
}
