package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonClient;

@Getter
@Setter
public class RedissonTopicConnectionSubscriberFactory extends AbstractConnectionSubscriberFactory {

    private RedissonClient redissonClient;

    private boolean shared;

    @Override
    public ConnectionSubscriber create(String scope) {
        return new RedissonTopicConnectionSubscriber(redissonClient, shared);
    }
}
