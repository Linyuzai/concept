package com.github.linyuzai.event.core.engine;

/**
 * 事件引擎扩展配置
 *
 * @param <E> 事件引擎类型
 */
public interface EventEngineConfigurer<E extends EventEngine> {

    /**
     * 配置事件引擎
     */
    void configure(E engine);
}
