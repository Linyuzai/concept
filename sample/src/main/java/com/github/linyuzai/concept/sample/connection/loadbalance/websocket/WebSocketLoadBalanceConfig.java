package com.github.linyuzai.concept.sample.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.EnableWebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
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
