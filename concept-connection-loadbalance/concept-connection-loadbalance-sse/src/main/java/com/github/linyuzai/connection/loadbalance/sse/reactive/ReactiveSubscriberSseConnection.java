package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import lombok.Getter;
import lombok.Setter;
import reactor.core.Disposable;

import java.util.function.Consumer;

@Getter
@Setter
public class ReactiveSubscriberSseConnection extends SseConnection {

    private Disposable disposable;

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {

    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
