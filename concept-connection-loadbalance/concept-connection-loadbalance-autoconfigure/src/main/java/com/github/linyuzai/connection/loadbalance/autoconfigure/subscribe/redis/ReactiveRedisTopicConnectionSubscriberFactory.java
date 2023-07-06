package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

@Getter
@Setter
public class ReactiveRedisTopicConnectionSubscriberFactory extends MasterSlaveConnectionSubscriberFactory {

    private ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new ReactiveRedisTopicConnectionSubscriber(reactiveRedisTemplate);
    }
}
