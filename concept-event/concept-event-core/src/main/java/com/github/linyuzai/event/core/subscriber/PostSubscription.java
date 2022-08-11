package com.github.linyuzai.event.core.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 在取消订阅后执行后置处理逻辑
 */
@Getter
@AllArgsConstructor
public class PostSubscription implements Subscription {

    private Subscription subscription;

    private Runnable runnable;

    @Override
    public void unsubscribe() {
        subscription.unsubscribe();
        if (runnable != null) {
            runnable.run();
        }
    }
}
