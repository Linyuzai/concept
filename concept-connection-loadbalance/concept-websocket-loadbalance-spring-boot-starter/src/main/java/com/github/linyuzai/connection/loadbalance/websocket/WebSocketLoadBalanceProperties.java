package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ws 配置
 */
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

    private ExecutorProperties executor = new ExecutorProperties();

    @Data
    public static class ServerProperties {

        /**
         * 默认服务端点配置
         */
        private DefaultEndpointProperties defaultEndpoint = new DefaultEndpointProperties();

        /**
         * 消息配置
         */
        private MessageProperties message = new MessageProperties();

        /**
         * 心跳配置
         */
        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        @Data
        public static class DefaultEndpointProperties {

            /**
             * 是否启用默认服务端点
             */
            private boolean enabled = true;

            /**
             * 默认端点前缀，默认值：/concept-websocket/
             */
            private String prefix = WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX;

            /**
             * 路径选择器
             */
            private PathSelectorProperties pathSelector = new PathSelectorProperties();

            /**
             * 用户选择器
             */
            private UserSelectorProperties userSelector = new UserSelectorProperties();

            @Data
            public static class PathSelectorProperties {

                /**
                 * 是否启用路径选择
                 */
                private boolean enabled = false;
            }

            @Data
            public static class UserSelectorProperties {

                /**
                 * 是否启用用户选择
                 */
                private boolean enabled = false;
            }
        }
    }

    @Data
    public static class LoadBalanceProperties {

        /**
         * 主订阅类型，默认 WEBSOCKET
         */
        private MasterSubscriber subscriberMaster = MasterSubscriber.WEBSOCKET;

        /**
         * 从订阅类型，默认 NONE
         */
        private SlaveSubscriber subscriberSlave = SlaveSubscriber.NONE;

        /**
         * 消息配置
         */
        private MessageProperties message = new MessageProperties();

        /**
         * 监控配置
         */
        private MonitorProperties monitor = new MonitorProperties();

        /**
         * 心跳配置
         */
        private HeartbeatProperties heartbeat = new HeartbeatProperties();

        public enum MasterSubscriber {

            NONE,

            WEBSOCKET,
            WEBSOCKET_SSL,

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
