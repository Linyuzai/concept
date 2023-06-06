package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerPredicatedDomainCollection;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Predicate;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerPredicatedDomainCollection<T extends DomainObject> extends SchrodingerPredicatedDomainCollection<T>
        implements DomainCollection<T>, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    protected final Class<? extends DomainCollection<?>> type;

    public ProxySchrodingerPredicatedDomainCollection(Class<? extends DomainCollection<?>> type,
                                                      @NonNull DomainCollection<T> collection,
                                                      @NonNull Predicate<T> predicate) {
        super(collection, predicate);
        this.type = type;
    }
}
