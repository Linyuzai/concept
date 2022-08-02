package com.github.linyuzai.event.core.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSubscription implements Subscription {

    private Subscription subscription;

    private Runnable runnable;

    @Override
    public boolean subscribed() {
        return subscription.subscribed();
    }

    @Override
    public void unsubscribe() {
        subscription.unsubscribe();
        if (runnable != null) {
            runnable.run();
        }
    }
}