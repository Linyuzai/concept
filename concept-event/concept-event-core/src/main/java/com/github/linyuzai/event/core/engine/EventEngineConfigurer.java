package com.github.linyuzai.event.core.engine;

public interface EventEngineConfigurer<E extends EventEngine> {

    void configure(E engine);
}
