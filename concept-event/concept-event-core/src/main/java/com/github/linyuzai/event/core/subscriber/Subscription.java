package com.github.linyuzai.event.core.subscriber;

/**
 * 订阅句柄
 */
public interface Subscription {

    Subscription EMPTY = () -> {
    };

    /**
     * 取消订阅
     */
    void unsubscribe();
}
