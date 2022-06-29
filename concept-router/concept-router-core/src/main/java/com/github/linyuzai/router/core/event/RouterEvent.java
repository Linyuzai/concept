package com.github.linyuzai.router.core.event;

import com.github.linyuzai.router.core.concept.Router;

/**
 * 路由事件
 */
public interface RouterEvent {

    /**
     * 获得路由
     *
     * @return 路由
     */
    Router getRouter();
}
