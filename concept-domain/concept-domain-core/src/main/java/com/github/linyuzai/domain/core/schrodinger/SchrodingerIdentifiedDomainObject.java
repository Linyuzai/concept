package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import lombok.*;

/**
 * 薛定谔模型代理
 */
@Getter
public class SchrodingerIdentifiedDomainObject<T extends DomainObject>
        extends AbstractSchrodingerDomainObject<T> implements DomainObject {

    @NonNull
    protected final String id;

    public SchrodingerIdentifiedDomainObject(@NonNull DomainContext context,
                                             @NonNull String id) {
        super(context);
        this.id = id;
    }

    /**
     * 获得被代理的对象
     */
    public T doGetTarget() {
        T domain = getRepository().get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainObjectType(), id);
        }
        return domain;
    }
}
