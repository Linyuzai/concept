package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import com.github.linyuzai.event.core.template.EventTemplate;

import java.util.Arrays;
import java.util.Collection;

/**
 * 事件概念
 */
public interface EventConcept {

    /**
     * 初始化
     * <p>
     * 会调用 {@link EventConceptLifecycleListener#onInitialize(EventConcept)}
     */
    void initialize();

    /**
     * 销毁
     * <p>
     * 会调用 {@link EventConceptLifecycleListener#onDestroy(EventConcept)}
     */
    void destroy();

    /**
     * 创建事件模版
     * <p>
     * 事件模版可以进行配置
     * <p>
     * 持有事件模版进行发布订阅可以复用配置
     */
    EventTemplate template();

    /**
     * 获得默认的事件交换机
     */
    EventExchange getExchange();

    /**
     * 设置默认的事件交换机
     */
    void setExchange(EventExchange exchange);

    /**
     * 获得默认的事件编码器
     */
    EventEncoder getEncoder();

    /**
     * 设置默认的事件编码器
     */
    void setEncoder(EventEncoder encoder);

    /**
     * 获得默认的事件解码器
     */
    EventDecoder getDecoder();

    /**
     * 设置默认的事件解码器
     */
    void setDecoder(EventDecoder decoder);

    /**
     * 获得默认的异常处理器
     */
    EventErrorHandler getErrorHandler();

    /**
     * 设置默认的异常处理器
     */
    void setErrorHandler(EventErrorHandler errorHandler);

    /**
     * 根据名称获得事件引擎
     *
     * @param name 事件引擎名称
     * @return 对应的事件引擎或 null
     */
    EventEngine getEngine(String name);

    /**
     * 获得所有的事件引擎
     */
    Collection<? extends EventEngine> getEngines();

    /**
     * 添加事件引擎
     */
    default void addEngines(EventEngine... engines) {
        addEngines(Arrays.asList(engines));
    }

    /**
     * 添加事件引擎
     */
    void addEngines(Collection<? extends EventEngine> engines);

    /**
     * 根据名称移除事件引擎
     */
    default void removeEngines(String... engines) {
        removeEngines(Arrays.asList(engines));
    }

    /**
     * 根据名称移除事件引擎
     */
    void removeEngines(Collection<String> engines);

    /**
     * 获得所有的生命周期监听器
     */
    Collection<? extends EventConceptLifecycleListener> getLifecycleListeners();

    /**
     * 添加生命周期监听器
     */
    default void addLifecycleListeners(EventConceptLifecycleListener... lifecycleListeners) {
        addLifecycleListeners(Arrays.asList(lifecycleListeners));
    }

    /**
     * 添加生命周期监听器
     */
    void addLifecycleListeners(Collection<? extends EventConceptLifecycleListener> lifecycleListeners);

    /**
     * 添加生命周期监听器
     */
    default void removeLifecycleListeners(EventConceptLifecycleListener... lifecycleListeners) {
        removeLifecycleListeners(Arrays.asList(lifecycleListeners));
    }

    /**
     * 添加生命周期监听器
     */
    void removeLifecycleListeners(Collection<? extends EventConceptLifecycleListener> lifecycleListeners);
}
