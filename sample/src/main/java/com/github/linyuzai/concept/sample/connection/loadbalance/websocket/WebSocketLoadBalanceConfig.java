package com.github.linyuzai.concept.sample.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageHandler;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.websocket.EnableWebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketEventListener;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketLoadBalanceConcept
public class WebSocketLoadBalanceConfig {

    @Bean
    public MessageHandler messageHandler() {
        return new WebSocketMessageHandler() {
            @Override
            public void onMessage(Message message, Connection connection,  ConnectionLoadBalanceConcept concept) {
                System.out.println("Message " + message.getPayload());
            }
        };
    }

    @Bean
    public ConnectionEventListener connectionEventListener() {
        return new WebSocketEventListener() {
            @Override
            public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
                if (event instanceof ConnectionEvent) {
                    Connection connection = ((ConnectionEvent) event).getConnection();
                    if (Connection.Type.CLIENT.equals(connection.getType())) {
                        if (event instanceof ConnectionEstablishEvent) {
                            System.out.println("Open " + connection.getMetadata());
                        } else if (event instanceof ConnectionCloseEvent) {
                            System.out.println("Close " + connection.getMetadata());
                        } else if (event instanceof ConnectionErrorEvent) {
                            System.out.println("Error " + connection.getMetadata());
                        }
                    }
                }
            }
        };
    }

    @Bean
    public ConnectionServerManager connectionServerManager() {
        return new ConnectionServerManager() {
            @Override
            public void add(ConnectionServer server, ConnectionLoadBalanceConcept concept) {

            }

            @Override
            public void remove(ConnectionServer server, ConnectionLoadBalanceConcept concept) {

            }

            @Override
            public void clear(ConnectionLoadBalanceConcept concept) {

            }

            @Override
            public boolean isEqual(ConnectionServer server1, ConnectionServer server2, ConnectionLoadBalanceConcept concept) {
                return false;
            }

            @Override
            public ConnectionServer getLocal(ConnectionLoadBalanceConcept concept) {
                return new ConnectionServer() {
                    @Override
                    public String getInstanceId() {
                        return "localhost";
                    }

                    @Override
                    public String getServiceId() {
                        return "localhost";
                    }

                    @Override
                    public String getHost() {
                        return "localhost";
                    }

                    @Override
                    public int getPort() {
                        return 0;
                    }

                    @Override
                    public Map<String, String> getMetadata() {
                        return Collections.emptyMap();
                    }

                    @Override
                    public URI getUri() {
                        return null;
                    }

                    @Override
                    public String getScheme() {
                        return null;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }
                };
            }

            @Override
            public List<ConnectionServer> getConnectionServers(ConnectionLoadBalanceConcept concept) {
                return Collections.emptyList();
            }
        };
    }
}
