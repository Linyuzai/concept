package com.github.linyuzai.event.core.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class ComposeSubscription implements Subscription {

    private Collection<Subscription> subscriptions;

    @Override
    public boolean subscribed() {
        for (Subscription subscription : subscriptions) {
            if (subscription.subscribed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void unsubscribe() {
        for (Subscription subscription : subscriptions) {
            if (subscription.subscribed()) {
                subscription.unsubscribe();
            }
        }
    }
}
