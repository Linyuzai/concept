package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ReactiveSseConnection extends SseConnection {

    private final FluxSink<ServerSentEvent<Object>> fluxSink;

    @SuppressWarnings("unchecked")
    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {

        if (message instanceof ServerSentEvent) {
            fluxSink.next((ServerSentEvent<Object>) message);
        } else {
            //fluxSink.next()
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        fluxSink.complete();
    }
}
