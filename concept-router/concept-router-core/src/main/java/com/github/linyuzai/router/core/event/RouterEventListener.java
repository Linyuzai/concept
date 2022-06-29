package com.github.linyuzai.router.core.event;

/**
 * 路由事件监听器
 */
public interface RouterEventListener {

    /**
     * 监听到路由事件
     *
     * @param event 路由事件
     */
    void onEvent(Object event);
}
