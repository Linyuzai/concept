package com.github.linyuzai.connection.loadbalance.sse;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SSE 配置。
 * <p>
 * SSE properties.
 */
@Data
@ConfigurationProperties(prefix = "concept.sse")
public class SseLoadBalanceProperties {

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
         * 默认服务端点配置。
         * <p>
         * Default server endpoint properties.
         */
        private DefaultEndpointProperties defaultEndpoint = new DefaultEndpointProperties();

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

        @Data
        public static class DefaultEndpointProperties {

            /**
             * 是否启用默认服务端点。
             * <p>
             * Whether to enable the default server endpoint.
             */
            private boolean enabled = true;

            /**
             * 默认端点前缀，默认值：/concept-sse/。
             * <p>
             * Default endpoint prefix, default value: /concept-sse/.
             */
            private String prefix;

            /**
             * 路径选择器。
             * <p>
             * Path selector.
             */
            private PathSelectorProperties pathSelector = new PathSelectorProperties();

            /**
             * 用户选择器。
             * <p>
             * User selector.
             */
            private UserSelectorProperties userSelector = new UserSelectorProperties();

            @Data
            public static class PathSelectorProperties {

                /**
                 * 是否启用路径选择。
                 * <p>
                 * Whether to enable path selection.
                 */
                private boolean enabled = false;
            }

            @Data
            public static class UserSelectorProperties {

                /**
                 * 是否启用用户选择。
                 * <p>
                 * Whether to enable user selection.
                 */
                private boolean enabled = false;
            }
        }
    }

    @Data
    public static class LoadBalanceProperties {

        /**
         * 主订阅类型，默认 SSE。
         * <p>
         * Subscriber type of master, default SSE.
         */
        private MasterSubscriber subscriberMaster = MasterSubscriber.SSE;

        /**
         * 从订阅类型，默认 NONE。
         * <p>
         * Subscriber type of slave, default NONE.
         */
        private SlaveSubscriber subscriberSlave = SlaveSubscriber.NONE;

        /**
         * 消息配置。
         * <p>
         * Message properties.
         */
        private MessageProperties message = new MessageProperties();

        /**
         * 监控配置。
         * <p>
         * Monitor properties.
         */
        private MonitorProperties monitor = new MonitorProperties();

        /**
         * 心跳配置。
         * <p>
         * Heartbeat properties.
         */
        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        public enum MasterSubscriber {

            NONE,

            SSE,
            SSE_SSL,

            REDISSON_TOPIC,
            REDISSON_TOPIC_REACTIVE,

            REDISSON_SHARED_TOPIC,
            REDISSON_SHARED_TOPIC_REACTIVE,

            REDIS_TOPIC,
            REDIS_TOPIC_REACTIVE,

            RABBIT_FANOUT,

            KAFKA_TOPIC
        }

        public enum SlaveSubscriber {

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

        @Data
        public static class MonitorProperties {

            /**
             * 是否启用订阅监控。
             * <p>
             * Whether to enable subscription monitoring.
             */
            private boolean enabled = true;

            /**
             * 是否打印订阅监控日志。
             * <p>
             * Whether to print subscription monitoring logs.
             */
            private boolean logger = false;

            /**
             * 订阅监控周期，毫秒。
             * <p>
             * Subscription monitoring cycle, milliseconds.
             */
            private long period = 30000;
        }
    }

    @Data
    public static class ExecutorProperties {

        /**
         * 线程池大小，默认值：1。
         * <p>
         * Thread pool size, default value: 1.
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
