package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;

@Getter
@Setter
public class RedisTopicConnectionSubscriberFactory extends MasterSlaveConnectionSubscriberFactory {

    private StringRedisTemplate redisTemplate;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new RedisTopicConnectionSubscriber(redisTemplate);
    }
}
