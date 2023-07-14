package com.github.linyuzai.connection.loadbalance.core.scope;

/**
 * 可以通过添加连接域来支持对应连接域的工厂。
 * <p>
 * The factory that can add scope to support scope.
 */
public abstract class AbstractScopedFactory<T> extends AbstractScoped implements ScopedFactory<T> {

}
