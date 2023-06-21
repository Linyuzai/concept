package com.github.linyuzai.connection.loadbalance.core.scope;

import lombok.Getter;

@Getter
public abstract class AbstractScopedFactory<T> extends AbstractScoped implements ScopedFactory<T> {

}
