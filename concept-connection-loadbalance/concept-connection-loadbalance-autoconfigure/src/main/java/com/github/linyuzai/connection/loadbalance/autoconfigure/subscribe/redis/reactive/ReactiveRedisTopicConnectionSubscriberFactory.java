package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis.reactive;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriberFactory;
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
public class ReactiveRedisTopicConnectionSubscriberFactory extends MasterSlaveConnectionSubscriberFactory {

    private ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new ReactiveRedisTopicConnectionSubscriber(reactiveRedisTemplate);
    }
}
