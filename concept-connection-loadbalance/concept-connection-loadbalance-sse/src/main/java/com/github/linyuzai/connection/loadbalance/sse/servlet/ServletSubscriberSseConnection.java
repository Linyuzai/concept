package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.sse.concept.SubscriberSseConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.HttpURLConnection;
import java.util.function.Consumer;

@Getter
@Setter
@RequiredArgsConstructor
public class ServletSubscriberSseConnection extends SubscriberSseConnection {

    private HttpURLConnection http;

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (http != null) {
                http.disconnect();
            }
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
