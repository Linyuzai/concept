package com.github.linyuzai.properties.refresh.core;


import com.github.linyuzai.properties.refresh.core.condition.ForceRefreshPropertiesCondition;
import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 刷新器管理器
 */
public class PropertiesRefresherManager {

    /**
     * 刷新器列表
     */
    @Getter
    private final List<PropertiesRefresher> refreshers = new ArrayList<>();

    /**
     * 默认使用全量刷新
     *
     * @param properties 刷新的配置属性源
     */
    public void refresh(PlatformProperties properties) {
        refresh(properties, new ForceRefreshPropertiesCondition());
    }

    /**
     * 根据给定的配置属性和刷新条件进行刷新
     *
     * @param properties 配置属性
     * @param condition  刷新条件
     */
    public void refresh(PlatformProperties properties, RefreshPropertiesCondition condition) {
        for (PropertiesRefresher refresher : refreshers) {
            refresher.refresh(properties);
        }
    }

    /**
     * 添加刷新器
     *
     * @param refresher 刷新器实例
     */
    public void addRefresher(PropertiesRefresher refresher) {
        this.refreshers.add(refresher);
    }

    /**
     * 清空刷新器
     */
    public void destroy() {
        this.refreshers.clear();
    }
}
