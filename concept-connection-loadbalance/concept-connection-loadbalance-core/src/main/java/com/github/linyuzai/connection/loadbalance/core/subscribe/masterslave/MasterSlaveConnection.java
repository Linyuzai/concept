package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.function.Consumer;

/**
 * 主从连接。
 * <p>
 * Master slave connection.
 */
public interface MasterSlaveConnection extends Connection {

    /**
     * 获取 master 连接。
     * <p>
     * Get master connection.
     */
    Connection getMaster();

    /**
     * 获取 slave 连接。
     * <p>
     * Get slave connection.
     */
    Connection getSlave();

    /**
     * 获取当前连接。
     * <p>
     * Get current connection.
     */
    Connection getCurrent();

    /**
     * 获取主从切换时间。
     * <p>
     * Get master slave switch timestamp.
     */
    long getSwitchTimestamp();

    /**
     * 是否是主连接。
     * <p>
     * If the master connection.
     */
    boolean isMaster(Connection connection);

    /**
     * 是否是从连接。
     * <p>
     * If the slave connection.
     */
    boolean isSlave(Connection connection);

    /**
     * 切换主从。
     * <p>
     * Switch master slave.
     */
    void switchover(Consumer<MasterSlaveSwitcher> consumer);

    /**
     * 主从切换器。
     * <p>
     * Master slave switcher.
     */
    interface MasterSlaveSwitcher {

        /**
         * 切换 master。
         * <p>
         * Switch master.
         *
         * @return 是否切换（并不是切换失败）
         */
        boolean switchMaster();

        /**
         * 切换 slave。
         * <p>
         * Switch slave.
         *
         * @return 是否切换（并不是切换失败）
         */
        boolean switchSlave();
    }
}
