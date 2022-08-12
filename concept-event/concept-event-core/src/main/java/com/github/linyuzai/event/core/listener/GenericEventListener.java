package com.github.linyuzai.event.core.listener;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.utils.GenericProvider;

import java.lang.reflect.Type;

/**
 * 基于泛型的事件监听器
 *
 * @param <T> 事件类型
 */
public abstract class GenericEventListener<T> implements EventListener, GenericProvider<Object> {

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(Object event, EventEndpoint endpoint, EventContext context) {
        onGenericEvent((T) event, endpoint, context);
    }

    /**
     * 基于反射获得事件类型
     */
    @Override
    public Type getType() {
        return getGenericType();
    }

    /**
     * 指定 {@link GenericEventListener} 上的泛型
     */
    @Override
    public Class<?> getTarget() {
        return GenericEventListener.class;
    }

    /**
     * 监听处理泛型后的事件
     */
    public abstract void onGenericEvent(T event, EventEndpoint endpoint, EventContext context);
}
