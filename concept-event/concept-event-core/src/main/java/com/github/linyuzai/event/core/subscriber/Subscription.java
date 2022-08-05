package com.github.linyuzai.event.core.subscriber;

public interface Subscription {

    Subscription EMPTY = () -> {
    };

    void unsubscribe();
}
