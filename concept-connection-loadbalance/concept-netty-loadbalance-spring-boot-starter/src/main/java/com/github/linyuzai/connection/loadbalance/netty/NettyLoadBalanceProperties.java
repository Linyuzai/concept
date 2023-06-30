package com.github.linyuzai.connection.loadbalance.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ws 配置
 */
@Data
@ConfigurationProperties(prefix = "concept.netty")
public class NettyLoadBalanceProperties {

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
         * 消息配置
         */
        private MessageProperties message = new MessageProperties();
    }

    @Data
    public static class LoadBalanceProperties {

        /**
         * 主订阅类型，默认 WEBSOCKET
         */
        private Subscriber subscriberMaster = Subscriber.NONE;

        /**
         * 从订阅类型，默认 NONE
         */
        private Subscriber subscriberSlave1 = Subscriber.NONE;

        /**
         * 消息配置
         */
        private MessageProperties message = new MessageProperties();

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

        public enum Subscriber {

            NONE,

            REDISSON_TOPIC,
            REDISSON_SHARED_TOPIC,

            REDIS_TOPIC,

            RABBIT_FANOUT,

            KAFKA_TOPIC
        }

        @Data
        public static class MonitorProperties {

            /**
             * 是否启用订阅监控
             */
            private boolean enabled = true;

            /**
             * 是否打印订阅监控日志
             */
            private boolean logger = false;

            /**
             * 订阅监控周期，毫秒
             */
            private long period = 30000;
        }
    }

    @Data
    public static class MessageProperties {

        /**
         * 重试配置
         */
        private RetryProperties retry = new RetryProperties();

        @Data
        public static class RetryProperties {

            /**
             * 重试次数
             */
            private int times;

            /**
             * 重试间隔，毫秒
             */
            private int period;
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
