package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ReactiveSseConnection extends SseConnection {

    private final FluxSink<ServerSentEvent<Object>> fluxSink;

    @SuppressWarnings("unchecked")
    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (message instanceof ServerSentEvent) {
                fluxSink.next((ServerSentEvent<Object>) message);
                onSuccess.run();
            } else {
                throw new IllegalArgumentException("Message is not a ServerSentEvent");
            }
        } catch (WebClientException e) {
            closeObservable();
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            fluxSink.complete();
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
