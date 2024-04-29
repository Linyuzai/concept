package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.SubscriberSseConnection;
import lombok.Getter;
import lombok.Setter;
import reactor.core.Disposable;

import java.util.function.Consumer;

@Getter
@Setter
public class ReactiveSubscriberSseConnection extends SubscriberSseConnection {

    private Disposable disposable;

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
