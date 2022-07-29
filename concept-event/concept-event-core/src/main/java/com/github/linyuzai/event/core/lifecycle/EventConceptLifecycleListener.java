package com.github.linyuzai.event.core.lifecycle;

import com.github.linyuzai.event.core.concept.EventConcept;

/**
 * 生命周期监听器
 */
public interface EventConceptLifecycleListener {

    /**
     * 监听初始化
     */
    void onInitialize(EventConcept concept);

    /**
     * 监听销毁
     */
    void onDestroy(EventConcept concept);
}
