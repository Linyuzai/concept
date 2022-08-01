package com.github.linyuzai.event.core.bus;

public interface EventBus {

    void initialize();

    void destroy();

    void publish(Object event);
}
