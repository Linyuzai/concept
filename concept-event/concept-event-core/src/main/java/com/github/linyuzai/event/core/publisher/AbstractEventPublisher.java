package com.github.linyuzai.event.core.publisher;

import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

/**
 * 事件发布器的抽象类
 * <p>
 * 处理事件编码
 */
public abstract class AbstractEventPublisher implements EventPublisher {

    @Override
    public void publish(Object event, EventEndpoint endpoint, EventContext context) {
        doPublish(event, endpoint, context);
    }

    public abstract void doPublish(Object event, EventEndpoint endpoint, EventContext context);
}
