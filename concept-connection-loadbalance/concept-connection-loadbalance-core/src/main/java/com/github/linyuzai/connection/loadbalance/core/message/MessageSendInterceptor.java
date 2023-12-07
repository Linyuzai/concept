package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

/**
 * 消息发送拦截器。
 * <p>
 * To intercept message before sending.
 */
public interface MessageSendInterceptor {

    /**
     * 拦截。
     * 返回 true 表示拦截，消息不会发送。
     * 返回 false 表示不拦截，消息继续发送。
     * <p>
     * Message will not be sent if return true.
     * Message will be sent if return false.
     */
    boolean intercept(Message message, Connection connection);
}
