package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.template.EventTemplate;

public interface EventBusConfigurer {

    void configure(EventTemplate template);
}
