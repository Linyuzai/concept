package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.websocket.load-balance")
public class WebSocketLoadBalanceProperties {

    private ServerProperties server = new ServerProperties();

    private SubscriberProperties subscriber = new SubscriberProperties();

    @Data
    public static class ServerProperties {

        private ServerType type = ServerType.AUTO;

        private boolean autoPong = true;

        private DefaultEndpointProperties defaultEndpoint = new DefaultEndpointProperties();

        @Data
        public static class DefaultEndpointProperties {

            private boolean enabled = true;
        }
    }

    @Data
    public static class SubscriberProperties {

        private String protocol = "ws";

        private boolean logger = true;

        private MonitorProperties monitor = new MonitorProperties();

        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        @Data
        public static class MonitorProperties {

            private boolean enabled = true;

            private long period = 30 * 1000;
        }

        @Data
        public static class HeartbeatProperties {

            private boolean enabled = false;

            private String ping;

            private String pong;

            private long period = 2 * 60 * 1000;
        }
    }
}
