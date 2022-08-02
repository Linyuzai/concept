package com.github.linyuzai.event.core.bus;

import com.github.linyuzai.event.core.config.InstanceConfig;

public interface EventBus extends InstanceConfig {

    void initialize();

    void destroy();

    void publish(Object event);
}
