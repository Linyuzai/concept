package com.github.linyuzai.event.core.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public Subscription simplify() {
        List<Subscription> all = new ArrayList<>();
        collect(this, all);
        List<Subscription> list = all.stream()
                .filter(it -> it != Subscription.EMPTY)
                .collect(Collectors.toList());
        int size = list.size();
        if (size == 0) {
            return Subscription.EMPTY;
        } else if (size == 1) {
            return list.get(0);
        } else if (size == subscriptions.size()) {
            return this;
        } else {
            return new ComposeSubscription(list);
        }
    }

    public void collect(Subscription subscription, Collection<Subscription> collection) {
        if (subscription instanceof ComposeSubscription) {
            for (Subscription s : ((ComposeSubscription) subscription).getSubscriptions()) {
                collect(s, collection);
            }
        } else {
            collection.add(subscription);
        }
    }
}
