package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerPredicatedDomainCollection;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerPredicatedDomainCollection<T extends DomainObject>
        extends SchrodingerPredicatedDomainCollection<T>
        implements DomainCollection<T>, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    @NonNull
    protected final Class<? extends DomainCollection<?>> type;

    public ProxySchrodingerPredicatedDomainCollection(@NonNull Class<? extends DomainCollection<?>> type,
                                                      @NonNull DomainCollection<T> collection,
                                                      @NonNull Predicate<T> predicate) {
        super(collection, predicate);
        this.type = type;
    }

    @Override
    public Conditions getConditions() {
        return withPropertyKey(Conditions.class, () ->
                Conditions.from(AccessAdapter.super.getConditions())
                        .in(Conditions.ID, list().stream()
                                .map(Identifiable::getId).collect(Collectors.toSet())));
    }
}
