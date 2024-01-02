package com.github.linyuzai.download.core.logger;

import com.github.linyuzai.download.core.compress.SourceCompressingProgressEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadStartedEvent;
import com.github.linyuzai.download.core.load.SourceLoadingProgressEvent;
import com.github.linyuzai.download.core.utils.DownloadUtils;
import com.github.linyuzai.download.core.web.ResponseWritingProgressEvent;
import com.github.linyuzai.download.core.write.Progress;
import com.github.linyuzai.download.core.write.ProgressEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 进度计算日志，包括加载进度，压缩进度，响应写入进度。
 */
@Getter
@Setter
@AllArgsConstructor
public class ProgressCalculationLogger extends LoggingDownloadEventListener {

    /**
     * 打印间隔
     */
    private Duration duration;

    /**
     * 是否使用百分比数据
     */
    private boolean percentage;

    /**
     * 进度事件回调。
     *
     * @param event 进度事件
     */
    public void onProgress(ProgressEvent event) {
        String s;
        if (event instanceof ResponseWritingProgressEvent) {
            s = "Writing response ";
        } else if (event instanceof SourceCompressingProgressEvent) {
            s = "Compressing source ";
        } else if (event instanceof SourceLoadingProgressEvent) {
            s = "Loading " + ((SourceLoadingProgressEvent) event).getSource().getDescription() + " ";
        } else {
            s = "Progressing ";
        }
        //log(event.getContext(), "\033[F");
        String msg = s + (percentage ? getPercentageMessage(event) : getRatioMessage(event));
        log(event.getContext(), msg);
    }

    /**
     * 如果存在总大小则返回百分比，
     * 否则返回当前进度。
     *
     * @return 百分比或当前进度
     */
    public String getPercentageMessage(ProgressEvent event) {
        Progress progress = event.getProgress();
        if (progress.hasTotal()) {
            double v = (progress.getCurrent() * 1.0 / progress.getTotal()) * 100.0;
            String format = String.format("%.2f", v);
            return format + "%";
        } else {
            return getCurrentMessage(progress);
        }
    }

    /**
     * 如果存在总大小则返回比值，
     * 否则返回当前进度。
     *
     * @return 比值或当前进度
     */
    public String getRatioMessage(ProgressEvent event) {
        Progress progress = event.getProgress();
        if (progress.hasTotal()) {
            return DownloadUtils.format(progress.getCurrent()) + "/" + DownloadUtils.format(progress.getTotal());
        } else {
            return getCurrentMessage(progress);
        }
    }

    /**
     * 返回当前进度的格式化数据。
     *
     * @return 当前进度
     */
    public String getCurrentMessage(Progress progress) {
        return DownloadUtils.format(progress.getCurrent());
    }

    /**
     * 监听到 {@link ProgressEvent} 事件时，打印更新进度。
     *
     * @param event 事件
     */
    @Override
    public void logOnEvent(Object event) {
        if (event instanceof DownloadStartedEvent) {
            DownloadContext context = ((DownloadStartedEvent) event).getContext();
            context.set(ProgressInterval.class, new ConcurrentHashMap<>());
        } else if (event instanceof ProgressEvent) {
            ProgressEvent pde = (ProgressEvent) event;
            DownloadContext context = ((ProgressEvent) event).getContext();
            Map<Object, ProgressInterval> map = context.get(ProgressInterval.class);
            if (map != null) {
                map.computeIfAbsent(getId(pde), o ->
                        new ProgressInterval(this::onProgress)).publish(pde);
            }
        }
    }

    /**
     * 根据事件获得id。
     *
     * @param event 事件
     * @return id
     */
    protected Object getId(ProgressEvent event) {
        if (event instanceof SourceLoadingProgressEvent) {
            return ((SourceLoadingProgressEvent) event).getSource();
        } else if (event instanceof ResponseWritingProgressEvent) {
            return ResponseWritingProgressEvent.class;
        } else {
            return ProgressEvent.class;
        }
    }

    /**
     * 进度间隔触发器。
     */
    public class ProgressInterval {

        private boolean last;

        private long start = System.nanoTime();

        /**
         * 更新回调
         */
        private final Consumer<ProgressEvent> consumer;

        /**
         * {@link ProgressEvent} 持有者
         */
        private final ProgressEventHolder holder = new ProgressEventHolder();

        public ProgressInterval(Consumer<ProgressEvent> consumer) {
            this.consumer = consumer;
        }

        /**
         * 发布事件更新。
         * 如果已经到达 100% 直接更新并不再订阅更新；
         * 否则根据 ready 值，true 更新，false 忽略。
         *
         * @param event 事件
         */
        public void publish(ProgressEvent event) {
            if (!last && event.getProgress().getTotal() != null &&
                    event.getProgress().getCurrent() == event.getProgress().getTotal()) {
                last = true;
                consumer.accept(event);
            } else {
                long time = System.nanoTime();
                if ((time - start >= duration.toNanos()) && holder.update(event)) {
                    consumer.accept(event);
                    start = time;
                }
            }
        }
    }

    /**
     * {@link ProgressEvent} 持有者。
     */
    @Data
    public static class ProgressEventHolder {

        /**
         * 持有的事件
         */
        private volatile ProgressEvent event;

        /**
         * 根据当前持有的事件和新事件判断是否需要更新。
         *
         * @param newEvent 新事件
         * @return 是否需要更新
         */
        public boolean update(ProgressEvent newEvent) {
            if (event == null) {
                event = newEvent;
                return true;
            } else {
                if (event.getProgress().getCurrent() == newEvent.getProgress().getCurrent()) {
                    return false;
                } else {
                    event = newEvent;
                    return true;
                }
            }
        }
    }
}
