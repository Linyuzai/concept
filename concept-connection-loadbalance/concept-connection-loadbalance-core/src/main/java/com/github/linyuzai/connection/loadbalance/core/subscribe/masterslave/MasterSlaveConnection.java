package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.function.Consumer;

public interface MasterSlaveConnection extends Connection {

    Connection getMaster();

    Connection getSlave();

    Connection getCurrent();

    long getSwitchTimestamp();

    boolean isMaster(Connection connection);

    boolean isSlave(Connection connection);

    void switchover(Consumer<MasterSlaveSwitcher> consumer);

    interface MasterSlaveSwitcher {

        /**
         * 切换 master
         *
         * @return 是否切换（并不是切换失败）
         */
        boolean switchMaster();

        /**
         * 切换 slave
         *
         * @return 是否切换（并不是切换失败）
         */
        boolean switchSlave();
    }
}
