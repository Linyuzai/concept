package com.github.linyuzai.connection.loadbalance.core.scope;

public interface ScopedFactory<T> extends Scoped {

    T create(String scope);
}
