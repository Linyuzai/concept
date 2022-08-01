package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.EventListener;

import java.util.function.Consumer;

/**
 * 事件订阅器的抽象类
 * <p>
 * 处理事件解码
 */
public abstract class AbstractEventSubscriber implements EventSubscriber {

    @Override
    public void subscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        doSubscribe(endpoint, context, o -> {
            EventDecoder decoder = context.get(EventDecoder.class);
            //解码事件
            Object decoded = decoder == null ? o : decoder.decode(o, context);
            listener.onEvent(decoded);
        });
    }


    public abstract void doSubscribe(EventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
