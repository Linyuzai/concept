package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;

@Getter
@Setter
public class RedisTopicConnectionSubscriberFactory extends MasterSlaveConnectionSubscriberFactory {

    private RedisTemplate<?, ?> redisTemplate;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new RedisTopicConnectionSubscriber(redisTemplate);
    }
}
