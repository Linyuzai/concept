package com.github.linyuzai.event.core.config;

/**
 * 继承处理器
 *
 * @param <C> 配置类型
 */
public interface InheritHandler<C extends EngineConfig> {

    void inherit(C engine);
}
