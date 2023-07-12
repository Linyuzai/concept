package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public interface MessageSendInterceptor {

    /**
     * 拦截。
     * 返回 true 表示拦截，消息不会发送。
     * 返回 false 表示不拦截，消息继续发送。
     */
    boolean intercept(Message message, Connection connection);
}
