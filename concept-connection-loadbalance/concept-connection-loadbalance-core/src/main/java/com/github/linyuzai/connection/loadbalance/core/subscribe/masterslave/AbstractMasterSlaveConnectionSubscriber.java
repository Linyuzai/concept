package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMasterSlaveConnectionSubscriber extends AbstractConnectionSubscriber
        implements MasterSlaveConnectionSubscriber {

    private MasterSlave masterSlave = MasterSlave.MASTER;
}
