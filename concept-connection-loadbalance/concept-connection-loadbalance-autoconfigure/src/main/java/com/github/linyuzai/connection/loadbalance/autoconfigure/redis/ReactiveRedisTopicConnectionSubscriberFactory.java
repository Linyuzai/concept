package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

@Getter
@Setter
public class ReactiveRedisTopicConnectionSubscriberFactory extends AbstractConnectionSubscriberFactory {

    private ReactiveRedisTemplate<?, Object> reactiveRedisTemplate;

    @Override
    public ConnectionSubscriber create(String scope) {
        return new ReactiveRedisTopicConnectionSubscriber(reactiveRedisTemplate);
    }
}
