package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.concept.EventOperator;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.engine.EventEngine;

import java.lang.reflect.Type;

public interface EventEndpoint extends EventOperator.ObjectConfig {

    String getName();

    EventEngine getEngine();

    void setEngine(EventEngine publisher);

    void publish(Object event, EventContext context);

    void subscribe(Type type, EventContext context);
}
