package com.github.linyuzai.event.core.listener;

import com.github.linyuzai.event.core.context.EventContext;

public interface EventListener {

    void onEvent(Object event, EventContext context);
}
