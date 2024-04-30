package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ServletSseConnection extends SseConnection {

    private final SseEmitter sseEmitter;

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            sseEmitter.send(message);
            onSuccess.run();
        } catch (IOException e) {
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
            sseEmitter.complete();
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
