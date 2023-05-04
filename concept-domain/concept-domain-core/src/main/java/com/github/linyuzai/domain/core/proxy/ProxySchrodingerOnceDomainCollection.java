package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerOnceDomainCollection;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Predicate;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerOnceDomainCollection<T extends DomainObject> extends SchrodingerOnceDomainCollection<T>
        implements DomainCollection<T>, DomainProxy {

    protected final Class<? extends DomainCollection<?>> type;

    public ProxySchrodingerOnceDomainCollection(Class<? extends DomainCollection<?>> type, @NonNull DomainCollection<T> collection, @NonNull Predicate<T> predicate) {
        super(collection, predicate);
        this.type = type;
    }

    protected Class<? extends T> getDomainType() {
        return DomainLink.collection(type);
    }
}
