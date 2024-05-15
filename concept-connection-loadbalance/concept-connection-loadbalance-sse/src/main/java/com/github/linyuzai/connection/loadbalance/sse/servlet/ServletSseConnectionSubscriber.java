package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SubscriberSseCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ServletSseConnectionSubscriber extends SseConnectionSubscriber<ServletSubscriberSseConnection> {

    private final SseIdGenerator sseIdGenerator;

    private final ServletSseLoadBalanceRunner servletSseLoadBalanceRunner;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept, Consumer<ServletSubscriberSseConnection> onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            ServletSubscriberSseConnection connection = new ServletSubscriberSseConnection();
            Object id = sseIdGenerator.generateId(null);
            connection.setCreateRequest(new SubscriberSseCreateRequest(id, getEndpoint()));
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("GET");
            http.connect();
            connection.setHttp(http);
            Runnable runnable = () -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        try {
                            if (line.startsWith("data:")) {
                                String data = line.substring(5);
                                while (data.startsWith(" ")) {
                                    data = data.substring(1);
                                }
                                concept.onMessage(connection, data);
                            }
                        } catch (Throwable e) {
                            concept.onError(connection, e);
                        }
                    }
                } catch (Throwable e) {
                    concept.onError(connection, e);
                } finally {
                    concept.onClose(connection, null);
                }
            };
            servletSseLoadBalanceRunner.run(runnable, uri, concept);
            onSuccess.accept(connection);
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public String getType() {
        return "servlet";
    }
}
