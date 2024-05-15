package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SubscriberSseCreateRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

@Deprecated
@Getter
@RequiredArgsConstructor
public class OkHttpSseConnectionSubscriber extends SseConnectionSubscriber<OkHttpSseConnection> {

    private final SseIdGenerator sseIdGenerator;

    private final OkHttpSseClientFactory sseClientFactory;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept, Consumer<OkHttpSseConnection> onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            OkHttpSseConnection connection = new OkHttpSseConnection();
            Object id = sseIdGenerator.generateId(null);
            connection.setCreateRequest(new SubscriberSseCreateRequest(id, getEndpoint()));
            OkHttpClient client = sseClientFactory.create();
            Request request = new Request.Builder()
                    .url(uri.toURL())
                    .build();
            EventSourceListener listener = new Listener(concept, connection, onSuccess);
            EventSource eventSource = EventSources.createFactory(client)
                    .newEventSource(request, listener);
            //connection.setEventSource(eventSource);
            //onSuccess.accept(connection);
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

    @Getter
    @RequiredArgsConstructor
    public static class Listener extends EventSourceListener {

        private final ConnectionLoadBalanceConcept concept;

        private final OkHttpSseConnection connection;

        private final Consumer<OkHttpSseConnection> onSuccess;

        @Override
        public void onOpen(@NonNull EventSource eventSource, @NonNull Response response) {
            connection.setEventSource(eventSource);
            onSuccess.accept(connection);
        }

        @Override
        public void onEvent(@NonNull EventSource eventSource, @Nullable String id, @Nullable String type, @NonNull String data) {
            concept.onMessage(connection, data);
        }

        @Override
        public void onClosed(@NonNull EventSource eventSource) {
            concept.onClose(connection, null);
        }

        @Override
        public void onFailure(@NonNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
            if (t == null) {
                if (response == null) {
                    concept.onError(connection, new IOException("No response"));
                } else {
                    ResponseBody body = response.body();
                    if (body == null) {
                        String message = response.message();
                        concept.onError(connection, new IOException(message));
                    } else {
                        try {
                            String string = body.string();
                            concept.onError(connection, new IOException(string));
                        } catch (IOException e) {
                            concept.onError(connection, e);
                        }
                    }
                }
            } else {
                concept.onError(connection, t);
            }
        }
    }
}
