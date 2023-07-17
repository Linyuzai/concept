package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonClient;

/**
 * Redisson (Shared) Topic 连接订阅器工厂。
 * <p>
 * Factory of {@link RedissonTopicConnectionSubscriber}.
 */
@Getter
@Setter
public class RedissonTopicConnectionSubscriberFactory
        extends AbstractMasterSlaveConnectionSubscriberFactory {

    private RedissonClient redissonClient;

    private boolean shared;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new RedissonTopicConnectionSubscriber(redissonClient, shared);
    }
}
