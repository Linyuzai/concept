package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.BeforeContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.SourceLoadingProgressEvent;
import com.github.linyuzai.download.core.web.ResponseWritingProgressEvent;
import com.github.linyuzai.download.core.write.AbstractProgressEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 进度计算日志，包括加载进度，压缩进度，响应写入进度。
 */
@AllArgsConstructor
public class ProgressCalculationLogger extends LoggingDownloadEventListener {

    /**
     * 进度缓存
     */
    private final Map<String, Map<Object, ProgressInterval>> progressIntervalMap = new ConcurrentHashMap<>();

    /**
     * 打印间隔
     */
    @Getter
    @Setter
    private Duration duration;

    /**
     * 是否使用百分比数据
     */
    @Getter
    @Setter
    private boolean percentage;

    public ProgressCalculationLogger() {
        this(Duration.ofSeconds(1), false);
    }

    public ProgressCalculationLogger(long second) {
        this(Duration.ofSeconds(second), false);
    }

    public ProgressCalculationLogger(boolean percentage) {
        this(Duration.ofSeconds(1), percentage);
    }

    /**
     * 进度事件回调。
     *
     * @param event 进度事件
     */
    public void onProgress(AbstractProgressEvent event) {
        log(event.getContext(), percentage ? event.getPercentageMessage() : event.getRatioMessage());
    }

    /**
     * 监听到 {@link DownloadContext} 销毁事件时，移除缓存；
     * 监听到 {@link AbstractProgressEvent} 事件时，打印更新进度。
     *
     * @param event 事件
     */
    @Override
    public void logOnEvent(Object event) {
        if (event instanceof BeforeContextDestroyedEvent) {
            DownloadContext context = ((BeforeContextDestroyedEvent) event).getContext();
            String id = context.getId();
            Map<Object, ProgressInterval> remove = progressIntervalMap.remove(id);
            if (remove != null) {
                remove.values().forEach(ProgressInterval::disposable);
            }
        } else if (event instanceof AbstractProgressEvent) {
            AbstractProgressEvent pde = (AbstractProgressEvent) event;
            DownloadContext context = ((AbstractProgressEvent) event).getContext();
            progressIntervalMap.computeIfAbsent(context.getId(), id ->
                    new ConcurrentHashMap<>()).computeIfAbsent(getId(pde), o ->
                    new ProgressInterval(this::onProgress)).publish(pde);
        }
    }

    /**
     * 根据事件获得id。
     *
     * @param event 事件
     * @return id
     */
    protected Object getId(AbstractProgressEvent event) {
        if (event instanceof SourceLoadingProgressEvent) {
            return ((SourceLoadingProgressEvent) event).getSource();
        } else if (event instanceof ResponseWritingProgressEvent) {
            return ResponseWritingProgressEvent.class;
        } else {
            return AbstractProgressEvent.class;
        }
    }

    /**
     * 进度间隔触发器。
     */
    public class ProgressInterval {

        /**
         * 为 true 可更新打印
         * 为 false 忽略更新
         */
        private volatile boolean ready = true;

        /**
         * 更新回调
         */
        private final Consumer<AbstractProgressEvent> consumer;

        /**
         * 间隔触发的 {@link Disposable}
         */
        private final Disposable disposable;

        /**
         * {@link AbstractProgressEvent} 持有者
         */
        private final ProgressEventHolder holder = new ProgressEventHolder();

        public ProgressInterval(Consumer<AbstractProgressEvent> consumer) {
            this.consumer = consumer;
            disposable = Flux.interval(duration).subscribe(unused -> ready = true);
        }

        /**
         * 发布事件更新。
         * 如果已经到达 100% 直接更新并不再订阅更新；
         * 否则根据 ready 值，true 更新，false 忽略。
         *
         * @param event 事件
         */
        public void publish(AbstractProgressEvent event) {
            if (event.getProgress().getTotal() != null &&
                    event.getProgress().getCurrent() == event.getProgress().getTotal()) {
                update(event);
                disposable();
            } else {
                if (ready) {
                    update(event);
                }
            }
        }

        /**
         * 更新回调。
         *
         * @param event 事件
         */
        private synchronized void update(AbstractProgressEvent event) {
            if (holder.update(event)) {
                ready = false;
                consumer.accept(event);
            }
        }

        /**
         * 取消订阅。
         */
        public void disposable() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            holder.reset();
        }
    }

    /**
     * {@link AbstractProgressEvent} 持有者。
     */
    @Data
    public static class ProgressEventHolder {

        /**
         * 持有的事件
         */
        private volatile AbstractProgressEvent event;

        /**
         * 根据当前持有的事件和新事件判断是否需要更新。
         *
         * @param newEvent 新事件
         * @return 是否需要更新
         */
        public boolean update(AbstractProgressEvent newEvent) {
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

        /**
         * 释放事件资源。
         */
        public void reset() {
            event = null;
        }
    }
}
