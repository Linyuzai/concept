package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Supplier;

/**
 * 薛定谔模型代理
 */
@Getter
public class SchrodingerDeferredDomainObject<T extends DomainObject>
        extends AbstractSchrodingerDomainObject<T> implements DomainObject {

    @NonNull
    protected final Supplier<T> supplier;

    public SchrodingerDeferredDomainObject(@NonNull DomainContext context,
                                           @NonNull Supplier<T> supplier) {
        super(context);
        this.supplier = supplier;
    }

    /**
     * 获得被代理的对象
     */
    protected T doGetTarget() {
        T domain = supplier.get();
        if (domain == null) {
            throw new DomainNotFoundException(getDomainObjectType());
        }
        return domain;
    }
}
