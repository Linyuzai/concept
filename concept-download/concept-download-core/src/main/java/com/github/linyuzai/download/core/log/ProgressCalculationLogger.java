package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.AfterContextDestroyedEvent;
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

@AllArgsConstructor
public class ProgressCalculationLogger extends DownloadLogger {

    private final Map<String, Map<Object, ProgressInterval>> progressIntervalMap = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private Duration duration;

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

    public void onProgress(AbstractProgressEvent event) {
        log(event.getContext(), percentage ? event.getPercentageMessage() : event.getRatioMessage());
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof AfterContextDestroyedEvent) {
            DownloadContext context = ((AfterContextDestroyedEvent) event).getContext();
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

    protected Object getId(AbstractProgressEvent event) {
        if (event instanceof SourceLoadingProgressEvent) {
            return ((SourceLoadingProgressEvent) event).getSource();
        } else if (event instanceof ResponseWritingProgressEvent) {
            return ResponseWritingProgressEvent.class;
        } else {
            return AbstractProgressEvent.class;
        }
    }

    public class ProgressInterval {

        private volatile boolean ready = true;

        private final Consumer<AbstractProgressEvent> consumer;

        private final Disposable disposable;

        private final ProgressEventHolder holder = new ProgressEventHolder();

        public ProgressInterval(Consumer<AbstractProgressEvent> consumer) {
            this.consumer = consumer;
            disposable = Flux.interval(duration).subscribe(unused -> ready = true);
        }

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

        private synchronized void update(AbstractProgressEvent event) {
            if (holder.update(event)) {
                ready = false;
                consumer.accept(event);
            }
        }

        public void disposable() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            holder.reset();
        }
    }

    @Data
    public static class ProgressEventHolder {

        private volatile AbstractProgressEvent event;

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

        public void reset() {
            event = null;
        }
    }
}
