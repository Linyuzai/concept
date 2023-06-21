package com.github.linyuzai.connection.loadbalance.core.scope;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Scoped {

    boolean support(String scope);

    static <S extends Scoped> List<S> filter(String scope, Collection<S> collection) {
        return collection.stream().filter(it -> it.support(scope)).collect(Collectors.toList());
    }
}
