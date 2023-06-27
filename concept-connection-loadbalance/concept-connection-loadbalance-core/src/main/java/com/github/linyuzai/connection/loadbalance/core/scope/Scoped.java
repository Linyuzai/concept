package com.github.linyuzai.connection.loadbalance.core.scope;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Scoped {

    boolean support(String scope);

    static <S extends Scoped> S filter(String scope, Class<S> type, Collection<S> collection) {
        for (S s : collection) {
            if (s.support(scope)) {
                return s;
            }
        }
        throw new ConnectionLoadBalanceException(type.getName() + " not found");
    }

    static <S extends Scoped> List<S> filter(String scope, Collection<S> collection) {
        return collection.stream().filter(it -> it.support(scope)).collect(Collectors.toList());
    }
}
