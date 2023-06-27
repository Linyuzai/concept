package com.github.linyuzai.connection.loadbalance.websocket;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Deprecated
public class SubscriberConditions {

    @Deprecated
    public static class RedissonTopic extends AnyNestedCondition {

        public RedissonTopic() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-master", havingValue = "REDISSON_TOPIC")
        static class Master {
        }

        @ConditionalOnProperty(value = "concept.websocket.load-balance.subscriber-slave", havingValue = "REDISSON_TOPIC")
        static class Slave {
        }
    }
}
