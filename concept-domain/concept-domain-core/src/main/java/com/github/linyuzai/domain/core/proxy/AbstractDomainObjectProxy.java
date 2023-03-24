package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.proxy.DomainObjectProxy;

import java.lang.reflect.Proxy;

/**
 * 领域代理抽象类
 */
public abstract class AbstractDomainObjectProxy implements DomainObjectProxy {

    /**
     * 被代理的领域模型
     */
    protected Object target;

    @Override
    public Object getInvokeObject() {
        if (this.target == null) {
            this.target = doGetTarget();
        }
        return this.target;
    }

    public abstract Object doGetTarget();
}
