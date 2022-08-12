package com.github.linyuzai.event.core.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.utils.GenericProvider;

/**
 * 事件发布器的抽象类
 * <p>
 * 处理事件编码
 */
public abstract class AbstractEventPublisher<E extends EventEndpoint>
        implements EventPublisher, GenericProvider<E> {

    /**
     * 如果适配事件端点则发布
     *
     * @param event    事件
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    @Override
    public void publish(Object event, EventEndpoint endpoint, EventContext context) {
        //适配
        E adapt = adapt(endpoint, context);
        //端点不适配直接返回
        if (adapt == null) {
            return;
        }
        doPublish(event, adapt, context);
    }

    /**
     * 适配事件端点
     */
    public E adapt(EventEndpoint endpoint, EventContext context) {
        return adaptGeneric(endpoint);
    }

    /**
     * 指定 {@link AbstractEventPublisher} 上的端点
     */
    @Override
    public Class<?> getTarget() {
        return AbstractEventPublisher.class;
    }

    /**
     * 执行发布
     */
    public abstract void doPublish(Object event, E endpoint, EventContext context);
}
