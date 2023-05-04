package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.ListableDomainCollection;
import lombok.NonNull;

import java.util.List;

public class ProxyListableDomainCollection<T extends DomainObject> extends ListableDomainCollection<T> implements DomainProxy {

    public ProxyListableDomainCollection(@NonNull List<T> list) {
        super(list);
    }
}
