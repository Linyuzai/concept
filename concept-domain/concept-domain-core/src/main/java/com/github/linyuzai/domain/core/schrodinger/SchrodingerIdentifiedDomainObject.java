package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import lombok.*;

/**
 * 薛定谔模型代理
 */
@Getter
@Setter
public class SchrodingerIdentifiedDomainObject<T extends DomainObject>
        extends AbstractSchrodingerDomainObject<T> implements DomainObject {

    protected String id;

    /**
     * 获得被代理的对象
     */
    protected T doGetTarget() {
        T domain = getRepository().get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainObjectType(), id);
        }
        return domain;
    }

    @Override
    protected void onRelease() {
        id = null;
    }
}
