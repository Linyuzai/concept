package com.github.linyuzai.event.core.factory;

public interface EventFactory {

    Object create(String type, String name, Object event);
}
