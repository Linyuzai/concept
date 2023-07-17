package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;

/**
 * Netty 事件监听器。
 * <p>
 * Netty event listener.
 */
public interface NettyEventListener extends ConnectionEventListener, NettyScoped {
}
