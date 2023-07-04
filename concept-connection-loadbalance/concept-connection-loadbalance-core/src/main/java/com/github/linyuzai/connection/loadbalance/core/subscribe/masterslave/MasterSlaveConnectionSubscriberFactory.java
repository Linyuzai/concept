package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MasterSlaveConnectionSubscriberFactory extends AbstractConnectionSubscriberFactory {

    private MasterSlave masterSlave;

    @Override
    public ConnectionSubscriber create(String scope) {
        MasterSlaveConnectionSubscriber subscriber = doCreate(scope);
        subscriber.setMasterSlave(masterSlave);
        return subscriber;
    }

    public abstract MasterSlaveConnectionSubscriber doCreate(String scope);
}
