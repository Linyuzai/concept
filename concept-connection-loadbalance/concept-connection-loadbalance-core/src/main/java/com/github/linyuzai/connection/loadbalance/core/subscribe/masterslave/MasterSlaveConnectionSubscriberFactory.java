package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;

/**
 * 主从连接订阅者工厂。
 * <p>
 * Factory of {@link MasterSlaveConnectionSubscriber}.
 */
public interface MasterSlaveConnectionSubscriberFactory extends ConnectionSubscriberFactory {

    @Override
    MasterSlaveConnectionSubscriber create(String scope);

    MasterSlave getMasterSlave();

    void setMasterSlave(MasterSlave masterSlave);
}
