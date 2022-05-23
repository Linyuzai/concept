package com.github.linyuzai.connection.loadbalance.autoconfigure.scope;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.cloud.context.scope.GenericScope;

import java.util.List;

public class ConnectionScopeRegister extends CustomScopeConfigurer {

    public ConnectionScopeRegister(List<ScopeName> sns) {
        addScope(ConnectionScope.NAME, new GenericScope());
        for (ScopeName sn : sns) {
            addScope(sn.getName(), new GenericScope());
        }
    }
}
