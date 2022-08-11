package com.github.linyuzai.event.core.context;

/**
 * 上下文工厂实现
 */
public class MapEventContextFactory implements EventContextFactory {

    @Override
    public EventContext create() {
        return new MapEventContext();
    }
}
