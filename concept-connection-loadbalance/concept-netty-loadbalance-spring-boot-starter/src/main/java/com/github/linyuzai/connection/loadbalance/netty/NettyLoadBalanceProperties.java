package com.github.linyuzai.connection.loadbalance.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * netty 配置
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

    private ExecutorProperties executor = new ExecutorProperties();

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
         * 主订阅类型，默认 NONE
         */
        private Subscriber subscriberMaster = Subscriber.NONE;

        /**
         * 从订阅类型，默认 NONE
         */
        private Subscriber subscriberSlave = Subscriber.NONE;

        /**
         * 消息配置
         */
        private MessageProperties message = new MessageProperties();

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
    }

    @Data
    public static class ExecutorProperties {

        private int size = 1;
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
