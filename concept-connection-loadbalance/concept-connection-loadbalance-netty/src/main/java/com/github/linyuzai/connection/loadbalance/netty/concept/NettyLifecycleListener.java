package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.LifecycleListener;

/**
 * Netty 生命周期监听器。
 * <p>
 * Netty lifecycle listener.
 */
public interface NettyLifecycleListener extends LifecycleListener, NettyEventListener {
}
