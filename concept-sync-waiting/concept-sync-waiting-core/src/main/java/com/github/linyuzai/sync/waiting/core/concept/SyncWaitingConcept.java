package com.github.linyuzai.sync.waiting.core.concept;

import com.github.linyuzai.sync.waiting.core.caller.SyncCaller;
import com.github.linyuzai.sync.waiting.core.configuration.SyncWaitingConfiguration;
import lombok.NonNull;

/**
 * 管理类，提供统一接口。
 */
public interface SyncWaitingConcept {

    /**
     * 阻塞等待，无等待时间限制。
     *
     * @param key    标识
     * @param caller 业务调用回调
     * @param <T>    值类型
     * @return 值
     */
    default <T> T waitSync(@NonNull Object key, @NonNull SyncCaller caller) {
        return waitSync(key, caller, 0);
    }

    /**
     * 阻塞等待，可限制等待时间。
     *
     * @param key         标识
     * @param caller      业务调用回调
     * @param waitingTime 等待时间，ms
     * @param <T>         值类型
     * @return 值
     */
    default <T> T waitSync(@NonNull Object key, @NonNull SyncCaller caller, long waitingTime) {
        return waitSync(key, caller, waitingTime, 0);
    }

    /**
     * 阻塞等待，可限制等待超时时间和队列时间。
     *
     * @param key         标识
     * @param caller      业务调用回调
     * @param waitingTime 等待时间，ms
     * @param queuingTime 队列时间，ms
     * @param <T>         值类型
     * @return 值
     */
    default <T> T waitSync(@NonNull Object key, @NonNull SyncCaller caller, long waitingTime, long queuingTime) {
        SyncWaitingConfiguration configuration = new SyncWaitingConfiguration.Builder()
                .waitingTime(waitingTime)
                .queuingTime(queuingTime)
                .build();
        return waitSync(key, caller, configuration);
    }

    /**
     * 阻塞等待，基于等待配置 {@link SyncWaitingConfiguration}。
     *
     * @param key           标识
     * @param caller        业务调用回调
     * @param configuration 等待配置 {@link SyncWaitingConfiguration}
     * @param <T>           值类型
     * @return 值
     */
    <T> T waitSync(@NonNull Object key, @NonNull SyncCaller caller, @NonNull SyncWaitingConfiguration configuration);

    /**
     * 异步唤醒。
     *
     * @param key   标识
     * @param value 值
     */
    void notifyAsync(@NonNull Object key, Object value);

    /**
     * 某个 key 是否在等待中
     *
     * @param key 标识
     * @return 如果在等待中则返回 true
     */
    boolean isWaiting(@NonNull Object key);


}
