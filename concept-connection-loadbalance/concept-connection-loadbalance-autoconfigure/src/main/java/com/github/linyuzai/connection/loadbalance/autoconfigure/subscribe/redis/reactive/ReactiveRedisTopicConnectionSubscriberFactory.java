package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.reactive;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

/**
 * Reactive Redis Topic 连接订阅器工厂。
 * <p>
 * Factory of {@link ReactiveRedisTopicConnectionSubscriber}.
 */
@Getter
@Setter
public class ReactiveRedisTopicConnectionSubscriberFactory
        extends AbstractMasterSlaveConnectionSubscriberFactory {

    private ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new ReactiveRedisTopicConnectionSubscriber(reactiveRedisTemplate);
    }
}
