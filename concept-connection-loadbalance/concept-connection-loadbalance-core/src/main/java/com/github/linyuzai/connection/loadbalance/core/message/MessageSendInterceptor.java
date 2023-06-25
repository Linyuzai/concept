package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public interface MessageSendInterceptor {

    boolean intercept(Message message, Connection connection);
}
