package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import lombok.Getter;
import lombok.Setter;
import okhttp3.sse.EventSource;

import java.util.function.Consumer;

@Getter
@Setter
public class OkHttpSseConnection extends SseConnection {

    private EventSource eventSource;

    public OkHttpSseConnection() {
        setType(Type.SUBSCRIBER);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {

    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (eventSource != null) {
                eventSource.cancel();
            }
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
