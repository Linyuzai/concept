package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import lombok.*;
import org.springframework.web.reactive.socket.client.*;

import java.net.URI;
import java.util.function.Consumer;

/**
 * {@link ReactiveWebSocketConnection} 的连接订阅者。
 * <p>
 * {@link ReactiveWebSocketConnection} connection subscriber.
 */
@Getter
@RequiredArgsConstructor
public class ReactiveWebSocketConnectionSubscriber extends
        WebSocketConnectionSubscriber<ReactiveWebSocketConnection> {

    private final ReactiveWebSocketClientFactory webSocketClientFactory;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept,
                            Consumer<ReactiveWebSocketConnection> onSuccess,
                            Consumer<Throwable> onError,
                            Runnable onComplete) {
        WebSocketClient client = webSocketClientFactory.create();
        ReactiveWebSocketSubscriberHandler handler =
                new ReactiveWebSocketSubscriberHandler(concept, (session, sink) -> {
                    ReactiveWebSocketConnection connection = new ReactiveWebSocketConnection(session, sink);
                    connection.setType(Connection.Type.SUBSCRIBER);
                    onSuccess.accept(connection);
                });
        client.execute(uri, handler)
                .doOnError(onError)
                .doOnTerminate(onComplete)
                .subscribe();
    }

    @Override
    public String getType() {
        return "reactive";
    }
}
