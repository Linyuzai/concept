package com.github.linyuzai.concept.sample.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageHandler;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.websocket.EnableWebSocketLoadBalanceConcept;
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
        return new MessageHandler() {
            @Override
            public void onMessage(Message message, Connection connection) {
                System.out.println("Message " + message.getPayload());
            }
        };
    }

    @Bean
    public ConnectionEventListener connectionEventListener() {
        return new ConnectionEventListener() {
            @Override
            public void onEvent(Object event) {
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
    public ConnectionServerProvider connectionServerProvider() {
        return new ConnectionServerProvider() {
            @Override
            public ConnectionServer getClient() {
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
            public List<ConnectionServer> getConnectionServers() {
                return Collections.emptyList();
            }
        };
    }
}
