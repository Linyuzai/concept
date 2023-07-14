package com.github.linyuzai.connection.loadbalance.core.scope;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 连接域抽象类。
 * 可以在 Set 中添加支持的连接域，根据 Set 中是否存在对应的连接域来判断是否支持。
 * <p>
 * Support scope by add scope to the scope set.
 */
@Getter
public abstract class AbstractScoped implements Scoped {

    private final Set<String> scopes = new LinkedHashSet<>();

    public <S> S addScopes(String... scopes) {
        return addScopes(Arrays.asList(scopes));
    }

    @SuppressWarnings("unchecked")
    public <S> S addScopes(Collection<String> scopes) {
        this.scopes.addAll(scopes);
        return (S) this;
    }

    @Override
    public boolean support(String scope) {
        return scopes.contains(scope);
    }
}
