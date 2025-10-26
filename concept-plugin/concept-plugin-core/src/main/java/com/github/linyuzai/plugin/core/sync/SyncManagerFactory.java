package com.github.linyuzai.plugin.core.sync;

/**
 * 同步管理器工厂
 */
public interface SyncManagerFactory {

    /**
     * 创建同步管理器
     */
    SyncManager create(Object o);
}
