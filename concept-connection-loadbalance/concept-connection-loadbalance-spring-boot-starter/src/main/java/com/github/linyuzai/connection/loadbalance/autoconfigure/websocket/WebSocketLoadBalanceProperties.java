package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.websocket")
public class WebSocketLoadBalanceProperties {

    /**
     * 类型
     */
    private WebSocketType type = WebSocketType.AUTO;

    /**
     * 服务配置
     */
    private ServerProperties server = new ServerProperties();

    /**
     * 订阅配置
     */
    private LoadBalanceProperties loadBalance = new LoadBalanceProperties();

    @Data
    public static class ServerProperties {

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

            private PathSelectorProperties pathSelector = new PathSelectorProperties();

            @Data
            public static class PathSelectorProperties {

                private boolean enabled = false;
            }
        }
    }

    @Data
    public static class LoadBalanceProperties {

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
             * 是否打印订阅监控日志
             */
            private boolean logger = true;

            /**
             * 订阅监控周期，毫秒
             */
            private long period = 30000;
        }
    }

    @Data
    public static class HeartbeatProperties {

        /**
         * 是否启用心跳
         */
        private boolean enabled = true;

        /**
         * 心跳超时时间，毫秒
         */
        private long timeout = 210000;

        /**
         * 心跳周期，毫秒
         */
        private long period = 60000;
    }
}
