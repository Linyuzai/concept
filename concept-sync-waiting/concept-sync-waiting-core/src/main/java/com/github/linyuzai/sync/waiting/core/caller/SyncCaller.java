package com.github.linyuzai.sync.waiting.core.caller;

/**
 * 调用自定义业务方法的接口。
 */
public interface SyncCaller {

    /**
     * 调用。
     *
     * @param key 标识
     */
    void call(Object key);
}
