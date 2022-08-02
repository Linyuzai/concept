package com.github.linyuzai.event.core.subscriber;

public interface Subscription {

    Subscription EMPTY = new Subscription() {

        @Override
        public boolean subscribed() {
            return false;
        }

        @Override
        public void unsubscribe() {

        }
    };

    boolean subscribed();

    void unsubscribe();

    default void unsubscribe(Runnable runnable) {
        unsubscribe();
        runnable.run();
    }
}
