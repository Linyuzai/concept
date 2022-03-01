package com.github.linyuzai.properties.refresh.core;

import com.github.linyuzai.properties.refresh.core.concept.ReflectPropertiesRefreshConcept;
import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;

/**
 * 刷新器
 *
 * @see PropertiesRefresherManager
 * @see ReflectPropertiesRefreshConcept
 */
public interface PropertiesRefresher {

    /**
     * 刷新
     *
     * @param properties 配置属性源
     * @throws Throwable 异常
     */
    void refresh(PlatformProperties properties);
}
