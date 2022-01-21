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
import reactor.core.publisher.FluxSink;

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

    public class ProgressInterval implements Consumer<FluxSink<AbstractProgressEvent>> {

        private FluxSink<AbstractProgressEvent> sink;

        private final Disposable disposable;

        private Disposable innerDisposable;

        private final ProgressEventHolder holder = new ProgressEventHolder();

        public ProgressInterval(Consumer<AbstractProgressEvent> consumer) {
            disposable = Flux.create(this).subscribe(consumer);
        }

        @Override
        public void accept(FluxSink<AbstractProgressEvent> sink) {
            this.sink = sink;
            innerDisposable = Flux.interval(duration).subscribe(unused -> update());
        }

        public void publish(AbstractProgressEvent event) {
            holder.set(event);
            if (event.getProgress().getTotal() != null &&
                    event.getProgress().getCurrent() == event.getProgress().getTotal()) {
                update();
                disposable();
            }
        }

        private void update() {
            if (holder.isUpdate()) {
                AbstractProgressEvent event = holder.get();
                if (event != null) {
                    sink.next(event);
                }
            }
        }

        public void disposable() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            if (innerDisposable != null && !innerDisposable.isDisposed()) {
                innerDisposable.dispose();
            }
            holder.reset();
        }
    }

    @Data
    public static class ProgressEventHolder {

        private volatile AbstractProgressEvent event;

        private volatile boolean update;

        public void set(AbstractProgressEvent newEvent) {
            if (event == null) {
                event = newEvent;
                update = true;
            } else {
                if (event.getProgress().getCurrent() == newEvent.getProgress().getCurrent()) {
                    update = false;
                } else {
                    event = newEvent;
                    update = true;
                }
            }
        }

        public AbstractProgressEvent get() {
            update = false;
            return event;
        }

        public void reset() {
            update = false;
            event = null;
        }
    }
}
