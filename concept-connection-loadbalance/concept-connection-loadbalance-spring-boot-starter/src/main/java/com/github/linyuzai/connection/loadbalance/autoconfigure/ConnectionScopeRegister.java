package com.github.linyuzai.connection.loadbalance.autoconfigure;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.cloud.context.scope.GenericScope;

public class ConnectionScopeRegister extends CustomScopeConfigurer {

    public ConnectionScopeRegister() {
        addScope("connection", new GenericScope());
        addScope("websocket", new GenericScope());
    }
}
