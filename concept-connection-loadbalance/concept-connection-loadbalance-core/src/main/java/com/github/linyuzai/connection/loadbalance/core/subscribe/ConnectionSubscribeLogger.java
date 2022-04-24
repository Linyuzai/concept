package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;

@AllArgsConstructor
public class ConnectionSubscribeLogger implements ConnectionEventListener {

    private Consumer<String> logger;

    @Override
    public void onEvent(Object event) {
        /*if (event instanceof ProxyConnectionEvent) {
            if (event instanceof ProxyConnectionOpenEvent) {
                ConnectionServer server = ((ProxyConnectionOpenEvent) event).getConnectionServer();
                logger.accept(appendTag("Proxy connect to " + server.getInstanceId()));
            } else if (event instanceof ProxyConnectionOpenErrorEvent) {
                ConnectionServer server = ((ProxyConnectionOpenErrorEvent) event).getConnectionServer();
                logger.accept(appendTag("Proxy fail to connect " + server.getInstanceId()));
            } else if (event instanceof ProxyConnectionCloseEvent) {
                Connection connection = ((ProxyConnectionCloseEvent) event).getConnection();
                ConnectionServer server = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
                logger.accept(appendTag("Proxy close by " + server.getInstanceId()));
            }
        }*/
    }

    public String appendTag(String msg) {
        return "LBWebsocket >> " + msg;
    }
}
