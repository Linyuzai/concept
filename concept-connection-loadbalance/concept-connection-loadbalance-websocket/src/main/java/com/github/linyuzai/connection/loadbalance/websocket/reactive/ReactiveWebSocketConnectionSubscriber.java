package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.exception.WebSocketLoadBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.client.StandardWebSocketClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ReactiveWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber {

    @Override
    public Connection doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept) {
        StandardWebSocketClient client = new StandardWebSocketClient();
        URI uri = getUri(server);
        Con connection = new Con();
        ReactiveSubscribeWebSocketHandler handler =
                new ReactiveSubscribeWebSocketHandler(concept, server, connection);
        client.execute(uri, handler).subscribe();
        return connection;
    }

    @Override
    public String getType() {
        return "reactive";
    }

    public static class Con implements Connection, Consumer<Connection> {

        private volatile Connection connection;

        private boolean needClose;

        private final List<Message> lazyMessages = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void send(Message message) {
            if (connection == null) {
                synchronized (this) {
                    if (connection == null) {
                        lazyMessages.add(message);
                    } else {
                        connection.send(message);
                    }
                }
            } else {
                connection.send(message);
            }
        }

        @Override
        public Object getId() {
            if (connection == null) {
                throw new WebSocketLoadBalanceException("Connection is not open");
            }
            return connection.getId();
        }

        @Override
        public Map<Object, Object> getMetadata() {
            if (connection == null) {
                throw new WebSocketLoadBalanceException("Connection is not open");
            }
            return connection.getMetadata();
        }

        @Override
        public MessageEncoder getMessageEncoder() {
            if (connection == null) {
                throw new WebSocketLoadBalanceException("Connection is not open");
            }
            return connection.getMessageEncoder();
        }

        @Override
        public MessageDecoder getMessageDecoder() {
            if (connection == null) {
                throw new WebSocketLoadBalanceException("Connection is not open");
            }
            return connection.getMessageDecoder();
        }

        @Override
        public void close() {
            if (connection == null) {
                synchronized (this) {
                    if (connection == null) {
                        needClose = true;
                    } else {
                        connection.close();
                    }
                }
            } else {
                connection.close();
            }
        }

        @Override
        public void accept(Connection connection) {
            synchronized (this) {
                this.connection = connection;
                if (needClose) {
                    close();
                }
                for (Message message : lazyMessages) {
                    send(message);
                }
                lazyMessages.clear();
            }
        }
    }
}
