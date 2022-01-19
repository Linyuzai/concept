package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.AfterContextDestroyedEvent;
import com.github.linyuzai.download.core.context.AfterContextInitializedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
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

    private final Map<String, ProgressInterval> progressIntervalMap = new ConcurrentHashMap<>();
    private final Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();

    public String getProgressMessage(ProgressDownloadEvent event) {
        if (event != null) {
            return event.getPercentageMessage();
        }
        return "Unknown";
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof AfterContextInitializedEvent) {
            DownloadContext context = ((AfterContextInitializedEvent) event).getContext();
            String id = context.getId();
            ProgressInterval interval = new ProgressInterval();
            Disposable disposable = Flux.create(interval).subscribe(ev ->
                    log(context, getProgressMessage(ev)));
            progressIntervalMap.put(id, interval);
            disposableMap.put(id, disposable);
        } else if (event instanceof AfterContextDestroyedEvent) {
            DownloadContext context = ((AfterContextDestroyedEvent) event).getContext();
            String id = context.getId();
            ProgressInterval interval = progressIntervalMap.remove(id);
            if (interval != null) {
                interval.release();
            }
            Disposable disposable = disposableMap.remove(id);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        } else if (event instanceof ProgressDownloadEvent) {
            ProgressDownloadEvent pde = (ProgressDownloadEvent) event;
            ProgressInterval interval = progressIntervalMap.get(pde.getContext().getId());
            if (interval != null) {
                interval.publish(pde);
            }
        }
    }

    public static class ProgressInterval implements Consumer<FluxSink<ProgressDownloadEvent>> {

        private final Flux<Long> flux = Flux.interval(Duration.ofSeconds(2));

        private FluxSink<ProgressDownloadEvent> sink;

        private Disposable disposable;

        @Getter
        private final ProgressEventHolder holder = new ProgressEventHolder();

        @Override
        public void accept(FluxSink<ProgressDownloadEvent> sink) {
            this.sink = sink;
            disposable = flux.subscribe(unused -> {
                if (!holder.isConsumed()) {
                    ProgressDownloadEvent event = holder.get();
                    if (event != null) {
                        sink.next(event);
                    }
                }
            });
        }

        public void publish(ProgressDownloadEvent event) {
            holder.set(event);
            if (event.getProgress().getCurrent() == event.getProgress().getTotal()) {
                sink.next(holder.get());
            }
        }

        public void release() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            holder.release();
        }
    }

    @Data
    public static class ProgressEventHolder {

        private volatile ProgressDownloadEvent event;

        private volatile boolean consumed;

        public synchronized void set(ProgressDownloadEvent newEvent) {
            if (event == null || event.getProgress().getCurrent() < newEvent.getProgress().getCurrent()) {
                event = newEvent;
                consumed = false;
            }
        }

        public synchronized ProgressDownloadEvent get() {
            consumed = true;
            return event;
        }

        public void release() {
            event = null;
        }
    }
}
