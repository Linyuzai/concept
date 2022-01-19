package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.AfterContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.SourceLoadingProgressEvent;
import com.github.linyuzai.download.core.web.ResponseWritingProgressEvent;
import com.github.linyuzai.download.core.write.ProgressDownloadEvent;
import lombok.Data;
import lombok.Getter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ProgressCalculationLogger extends DownloadLogger {

    private final Map<String, Map<Object, ProgressInterval>> progressIntervalMap = new ConcurrentHashMap<>();

    public void onProgress(ProgressDownloadEvent event) {
        log(event.getContext(), event.getPercentageMessage());
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
        } else if (event instanceof ProgressDownloadEvent) {
            ProgressDownloadEvent pde = (ProgressDownloadEvent) event;
            DownloadContext context = ((ProgressDownloadEvent) event).getContext();
            progressIntervalMap.computeIfAbsent(context.getId(), id ->
                    new ConcurrentHashMap<>()).computeIfAbsent(getId(pde), o ->
                    new ProgressInterval(this::onProgress)).publish(pde);
        }
    }

    protected Object getId(ProgressDownloadEvent event) {
        if (event instanceof SourceLoadingProgressEvent) {
            return ((SourceLoadingProgressEvent) event).getSource();
        } else if (event instanceof ResponseWritingProgressEvent) {
            return ResponseWritingProgressEvent.class;
        } else {
            return ProgressDownloadEvent.class;
        }
    }

    public static class ProgressInterval implements Consumer<FluxSink<ProgressDownloadEvent>> {

        private FluxSink<ProgressDownloadEvent> sink;

        private final Disposable disposable;

        private Disposable innerDisposable;

        @Getter
        private final ProgressEventHolder holder = new ProgressEventHolder();

        public ProgressInterval(Consumer<ProgressDownloadEvent> consumer) {
            disposable = Flux.create(this).subscribe(consumer);
        }

        @Override
        public void accept(FluxSink<ProgressDownloadEvent> sink) {
            this.sink = sink;
            innerDisposable = Flux.interval(Duration.ofSeconds(2)).subscribe(unused -> {
                ProgressDownloadEvent event = holder.get();
                if (event != null) {
                    sink.next(event);
                }
            });
        }

        public void publish(ProgressDownloadEvent event) {
            holder.set(event);
            if (event.getProgress().getCurrent() == event.getProgress().getTotal()) {
                sink.next(holder.get());
                disposable();
            }
        }

        public void disposable() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            if (innerDisposable != null && !innerDisposable.isDisposed()) {
                innerDisposable.dispose();
            }
        }
    }

    @Data
    public static class ProgressEventHolder {

        private volatile ProgressDownloadEvent event;

        public void set(ProgressDownloadEvent newEvent) {
            event = newEvent;
        }

        public ProgressDownloadEvent get() {
            return event;
        }
    }
}
