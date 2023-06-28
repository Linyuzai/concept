package com.github.linyuzai.connection.loadbalance.core.subscribe;

public interface MasterSlaveConnectionSubscriber extends ConnectionSubscriber {

    void setMasterSlave(MasterSlave masterSlave);
}
