package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;

/**
 * 主从连接订阅者。
 * <p>
 * Master slave connection subscriber.
 */
public interface MasterSlaveConnectionSubscriber extends ConnectionSubscriber {

    MasterSlave getMasterSlave();

    void setMasterSlave(MasterSlave masterSlave);
}
