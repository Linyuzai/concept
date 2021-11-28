package com.github.linyuzai.sync.waiting.core.concept;

public interface SyncWaiter {

    Object key();

    void key(Object key);

    <T> T value();

    void value(Object value);

    void performWait(long time);

    void performNotify();
}
