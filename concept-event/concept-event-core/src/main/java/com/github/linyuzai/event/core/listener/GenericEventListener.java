package com.github.linyuzai.event.core.listener;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 基于泛型的事件监听器
 *
 * @param <T> 事件类型
 */
public abstract class GenericEventListener<T> implements EventListener {

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
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types.length == 1) {
                return types[0];
            }
        }
        return null;
    }

    /**
     * 监听处理泛型后的事件
     */
    public abstract void onGenericEvent(T event, EventEndpoint endpoint, EventContext context);
}
