package com.github.linyuzai.event.core.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;

/**
 * 组合事件发布器
 */
@Getter
@AllArgsConstructor
public class ComposeEventPublisher implements EventPublisher {

    /**
     * 多个事件发布器
     */
    private final Collection<EventPublisher> publishers;

    public ComposeEventPublisher(EventPublisher... publishers) {
        this(Arrays.asList(publishers));
    }

    /**
     * 遍历所有的事件发布器发布事件
     *
     * @param event    事件
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    @Override
    public void publish(Object event, EventEndpoint endpoint, EventContext context) {
        for (EventPublisher publisher : publishers) {
            publisher.publish(event, endpoint, context);
        }
    }
}
