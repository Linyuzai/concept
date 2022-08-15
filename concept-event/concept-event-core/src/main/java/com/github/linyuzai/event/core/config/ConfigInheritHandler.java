package com.github.linyuzai.event.core.config;

/**
 * 继承处理器
 *
 * @param <C> 配置类型
 */
public interface ConfigInheritHandler<C extends EngineConfig> {

    /**
     * 继承
     */
    void inherit(C config);
}
