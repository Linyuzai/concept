package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainObject;

import java.io.Serializable;

/**
 * 领域对象
 */
public interface DomainObject extends Identifiable, Serializable {

    static <T extends DomainObject> T schrodinger(Class<T> cls, String id, DomainContext context) {
        return new SchrodingerDomainObject(cls, id, context).create(cls);
    }
}
