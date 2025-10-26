package com.github.linyuzai.plugin.autoconfigure.management;

/**
 * 管理页面授权器
 */
public interface PluginManagementAuthorizer {

    /**
     * 解锁
     *
     * @param password 密码
     * @return 是否解锁
     */
    boolean unlock(String password);
}
