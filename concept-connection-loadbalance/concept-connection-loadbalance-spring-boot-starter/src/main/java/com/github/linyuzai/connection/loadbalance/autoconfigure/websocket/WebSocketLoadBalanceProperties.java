package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.websocket.load-balance")
public class WebSocketLoadBalanceProperties {

    /**
     * 服务配置
     */
    private ServerProperties server = new ServerProperties();

    /**
     * 订阅配置
     */
    private SubscriberProperties subscriber = new SubscriberProperties();

    @Data
    public static class ServerProperties {

        /**
         * 服务类型
         */
        private ServerType type = ServerType.AUTO;

        /**
         * 默认服务配置
         */
        private DefaultEndpointProperties defaultEndpoint = new DefaultEndpointProperties();

        /**
         * 心跳配置
         */
        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        @Data
        public static class DefaultEndpointProperties {

            /**
             * 是否启用默认服务
             */
            private boolean enabled = true;
        }
    }

    @Data
    public static class SubscriberProperties {

        /**
         * 订阅协议
         */
        private String protocol = "ws";

        /**
         * 订阅日志
         */
        private boolean logger = true;

        /**
         * 监控配置
         */
        private MonitorProperties monitor = new MonitorProperties();

        /**
         * 心跳配置
         */
        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        @Data
        public static class MonitorProperties {

            /**
             * 是否启用订阅监控
             */
            private boolean enabled = true;

            /**
             * 订阅监控周期
             */
            private long period = 30 * 1000;
        }
    }

    @Data
    public static class HeartbeatProperties {

        /**
         * 是否启用心跳
         */
        private boolean enabled = true;

        /**
         * 是否服务发心跳给客户端
         *
         * 默认由客户端发心跳
         */
        private boolean serverToClient = true;

        /**
         * ping帧
         */
        private String pingFrame = "";

        /**
         * pong帧
         */
        private String pongFrame = "";

        /**
         * 心跳超时时间
         */
        private long timeout = 3 * 60 * 1000;

        /**
         * 心跳周期
         */
        private long period = 60 * 1000;
    }
}
