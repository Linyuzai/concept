package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import static com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer.LB_HOST_PORT;

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
            onError.accept(new MessageTransportException(e));
            if (isObservableType()) {
                String lbHostPost = (String) getMetadata().get(LB_HOST_PORT);
                if (lbHostPost == null) {
                    return;
                }
                concept.onClose(this, Close.NOT_ALIVE);
                Collection<Connection> connections = concept.getConnectionRepository()
                        .select(Type.SUBSCRIBER);
                for (Connection connection : connections) {
                    ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                    if (server == null) {
                        continue;
                    }
                    String url = ConnectionServer.url(server);
                    if (Objects.equals(url, lbHostPost)) {
                        concept.onClose(connection, Close.NOT_ALIVE);
                        break;
                    }
                }
            }
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
