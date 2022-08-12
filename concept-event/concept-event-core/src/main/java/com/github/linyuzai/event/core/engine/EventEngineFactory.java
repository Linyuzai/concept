package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.config.EngineConfig;

/**
 * 事件引擎工厂
 *
 * @param <C> 事件引擎配置类型
 * @param <E> 事件引擎类型
 */
public interface EventEngineFactory<C extends EngineConfig, E extends EventEngine> {

    /**
     * 创建事件引擎
     */
    E create(C config);
}
