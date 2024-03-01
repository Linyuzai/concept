package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class OkHttpSseConnectionSubscriber extends SseConnectionSubscriber<OkHttpSseConnection> {

    private final OkHttpSseClientFactory sseClientFactory;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept, Consumer<OkHttpSseConnection> onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            OkHttpSseConnection connection = new OkHttpSseConnection();
            OkHttpClient client = sseClientFactory.create();
            Request request = new Request.Builder().url(uri.toURL()).build();
            EventSourceListener listener = new EventSourceListener() {

                @Override
                public void onOpen(@NonNull EventSource eventSource, @NonNull Response response) {
                    concept.onEstablish(connection);
                }

                @Override
                public void onEvent(@NonNull EventSource eventSource, @Nullable String id, @Nullable String type, @NonNull String data) {
                    concept.onMessage(connection, eventSource);
                }

                @Override
                public void onClosed(@NonNull EventSource eventSource) {
                    concept.onClose(connection, null);
                }

                @Override
                public void onFailure(@NonNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                    concept.onError(connection, t);
                }
            };

            EventSource eventSource = EventSources.createFactory(client)
                    .newEventSource(request, listener);
            connection.setEventSource(eventSource);
            onSuccess.accept(connection);
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public String getType() {
        return "okhttp";
    }
}
