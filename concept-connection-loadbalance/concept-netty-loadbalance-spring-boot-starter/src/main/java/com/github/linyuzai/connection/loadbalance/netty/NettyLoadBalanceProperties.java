package com.github.linyuzai.connection.loadbalance.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * netty 配置。
 * <p>
 * Netty properties.
 */
@Data
@ConfigurationProperties(prefix = "concept.netty")
public class NettyLoadBalanceProperties {

    /**
     * 服务配置。
     * <p>
     * Server properties.
     */
    private ServerProperties server = new ServerProperties();

    /**
     * 订阅配置。
     * <p>
     * Subscriber properties.
     */
    private LoadBalanceProperties loadBalance = new LoadBalanceProperties();

    /**
     * 执行器配置。
     * <p>
     * Executor properties.
     */
    private ExecutorProperties executor = new ExecutorProperties();

    @Data
    public static class ServerProperties {

        /**
         * 消息配置。
         * <p>
         * Message properties.
         */
        private MessageProperties message = new MessageProperties();
    }

    @Data
    public static class LoadBalanceProperties {

        /**
         * 主订阅类型，默认 NONE。
         * <p>
         * Subscriber type of master, default NONE.
         */
        private Subscriber subscriberMaster = Subscriber.NONE;

        /**
         * 从订阅类型，默认 NONE。
         * <p>
         * Subscriber type of slave, default NONE.
         */
        private Subscriber subscriberSlave = Subscriber.NONE;

        /**
         * 消息配置。
         * <p>
         * Message properties.
         */
        private MessageProperties message = new MessageProperties();

        /**
         * 心跳配置。
         * <p>
         * Heartbeat properties.
         */
        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        public enum Subscriber {

            NONE,

            REDISSON_TOPIC,
            REDISSON_TOPIC_REACTIVE,

            REDISSON_SHARED_TOPIC,
            REDISSON_SHARED_TOPIC_REACTIVE,

            REDIS_TOPIC,
            REDIS_TOPIC_REACTIVE,

            RABBIT_FANOUT,

            KAFKA_TOPIC
        }
    }

    @Data
    public static class ExecutorProperties {

        /**
         * 线程池大小，默认 1。
         * <p>
         * Thread pool size, default 1.
         */
        private int threadPoolSize = 1;
    }

    @Data
    public static class MessageProperties {

        /**
         * 重试配置。
         * <p>
         * Retry properties.
         */
        private RetryProperties retry = new RetryProperties();

        @Data
        public static class RetryProperties {

            /**
             * 重试次数。
             * <p>
             * Retry times.
             */
            private int times;

            /**
             * 重试间隔，毫秒。
             * <p>
             * Retry interval, milliseconds.
             */
            private int period;
        }
    }

    @Data
    public static class HeartbeatProperties {

        /**
         * 是否启用心跳。
         * <p>
         * Whether to enable heartbeat.
         */
        private boolean enabled = true;

        /**
         * 心跳超时时间，毫秒。
         * <p>
         * Heartbeat timeout, milliseconds.
         */
        private long timeout = 210000;

        /**
         * 心跳周期，毫秒。
         * <p>
         * Heartbeat period, milliseconds.
         */
        private long period = 60000;
    }
}
