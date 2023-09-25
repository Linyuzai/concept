package com.github.linyuzai.connection.loadbalance.core.concept;

public interface ConnectionCloseInterceptor {

    boolean intercept(Object reason, Connection connection);
}
