package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.ListableDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProxyListableDomainCollection<T extends DomainObject> extends ListableDomainCollection<T>
        implements DomainProxy, DomainProxy.CollectionAccess<T>, DomainProxy.ExtraAccess<Object> {

    protected Object extra;

    public ProxyListableDomainCollection(@NonNull List<T> list) {
        super(list);
    }

    @Override
    public DomainCollection<T> getCollection() {
        return this;
    }
}
