package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * 主从连接订阅者工厂。
 * <p>
 * Factory of {@link MasterSlaveConnectionSubscriber}.
 */
@Getter
@Setter
public abstract class AbstractMasterSlaveConnectionSubscriberFactory
        extends AbstractConnectionSubscriberFactory
        implements MasterSlaveConnectionSubscriberFactory {

    private MasterSlave masterSlave;

    @Override
    public MasterSlaveConnectionSubscriber create(String scope) {
        MasterSlaveConnectionSubscriber subscriber = doCreate(scope);
        subscriber.setMasterSlave(masterSlave);
        return subscriber;
    }

    public abstract MasterSlaveConnectionSubscriber doCreate(String scope);
}
