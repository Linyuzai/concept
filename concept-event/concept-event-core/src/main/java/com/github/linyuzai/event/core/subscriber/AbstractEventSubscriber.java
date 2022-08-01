package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public abstract class AbstractEventSubscriber implements EventSubscriber {

    @Override
    public void subscribe(Consumer<Object> consumer, EventEndpoint endpoint, EventContext context) {
        doSubscribe(endpoint, context, o -> {
            if (!context.contains(Type.class)) {
                synchronized (context) {
                    if (!context.contains(Type.class)) {
                        Type type = consumer.getClass().getGenericSuperclass();
                        if (type instanceof ParameterizedType) {
                            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                            if (types.length == 1) {
                                context.put(Type.class, types[0]);
                            }
                        }
                    }
                }
            }
            EventDecoder decoder = context.get(EventDecoder.class);
            //解码事件
            Object decoded = decoder == null ? o : decoder.decode(o, context);
            consumer.accept(decoded);
        });
    }

    public abstract void doSubscribe(EventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
