package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ServletSseConnection extends SseConnection {

    private final SseCreateRequest createRequest;

    private final SseEmitter sseEmitter;

    @Override
    public Object getId() {
        return createRequest.getId();
    }

    @Override
    public String getPath() {
        return createRequest.getPath();
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (message instanceof SseEmitter.SseEventBuilder) {
                sseEmitter.send((SseEmitter.SseEventBuilder) message);
            } else {
                sseEmitter.send(message);
            }
            onSuccess.run();
        } catch (IOException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        sseEmitter.complete();
    }
}
