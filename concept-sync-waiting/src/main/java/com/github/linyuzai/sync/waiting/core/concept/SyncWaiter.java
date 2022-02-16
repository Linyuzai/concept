package com.github.linyuzai.sync.waiting.core.concept;

/**
 * 同步等待器，与每个请求一一对应。
 */
public interface SyncWaiter {

    /**
     * 获得标识。
     *
     * @return 标识
     */
    Object key();

    /**
     * 设置标识。
     *
     * @param key 标识
     */
    void key(Object key);

    /**
     * 获得值。
     *
     * @param <T> 值类型
     * @return 值
     */
    <T> T value();

    /**
     * 设置值。
     *
     * @param value 值
     */
    void value(Object value);

    /**
     * 阻塞等待。
     *
     * @param time 等待超时时间
     */
    void performWait(long time);

    /**
     * 通知唤醒。
     */
    void performNotify();
}
