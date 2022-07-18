package com.github.linyuzai.event.core.context;

public class DefaultEventContextFactory implements EventContextFactory {
    @Override
    public EventContext create() {
        return new MapEventContext();
    }
}
