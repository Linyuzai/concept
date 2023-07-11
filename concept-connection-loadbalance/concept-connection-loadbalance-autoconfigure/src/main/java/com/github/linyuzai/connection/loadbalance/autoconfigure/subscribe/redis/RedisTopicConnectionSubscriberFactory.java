package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis Topic 连接订阅器工厂。
 * <p>
 * Factory of {@link RedisTopicConnectionSubscriber}.
 */
@Getter
@Setter
public class RedisTopicConnectionSubscriberFactory extends MasterSlaveConnectionSubscriberFactory {

    private StringRedisTemplate redisTemplate;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new RedisTopicConnectionSubscriber(redisTemplate);
    }
}
