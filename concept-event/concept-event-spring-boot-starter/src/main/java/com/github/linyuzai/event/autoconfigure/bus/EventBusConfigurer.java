package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.EventBus;

public interface EventBusConfigurer {

    void configure(EventBus bus);
}
