package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * 薛定谔模型代理
 */
@Setter
@Getter
public class SchrodingerDeferredDomainObject<T extends DomainObject>
        extends AbstractSchrodingerDomainObject<T> implements DomainObject {

    protected Supplier<T> supplier;

    /**
     * 获得被代理的对象
     */
    @Override
    protected T doGetTarget() {
        T domain = supplier.get();
        if (domain == null) {
            throw new DomainNotFoundException(getDomainObjectType());
        }
        return domain;
    }

    @Override
    protected void onRelease() {
        supplier = null;
    }
}
