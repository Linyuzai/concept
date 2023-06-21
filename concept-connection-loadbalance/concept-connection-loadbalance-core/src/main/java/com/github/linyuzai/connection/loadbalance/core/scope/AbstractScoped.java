package com.github.linyuzai.connection.loadbalance.core.scope;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public abstract class AbstractScoped implements Scoped {

    private final Set<String> scopes = new LinkedHashSet<>();

    public <F> F addScopes(String... scopes) {
        return addScopes(Arrays.asList(scopes));
    }

    @SuppressWarnings("unchecked")
    public <F> F addScopes(Collection<String> scopes) {
        this.scopes.addAll(scopes);
        return (F) this;
    }

    @Override
    public boolean support(String scope) {
        return scopes.contains(scope);
    }
}
