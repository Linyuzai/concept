package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;

@Getter
@Setter
public class RedisTopicConnectionSubscriberFactory extends AbstractConnectionSubscriberFactory {

    private RedisTemplate<?, ?> redisTemplate;

    @Override
    public ConnectionSubscriber create(String scope) {
        return new RedisTopicConnectionSubscriber(redisTemplate);
    }
}
