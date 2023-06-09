package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * 薛定谔的集合模型
 */
@Getter
public class SchrodingerDeferredDomainCollection<T extends DomainObject>
        extends AbstractSchrodingerDomainCollection<T> implements DomainCollection<T> {

    @NonNull
    protected final Supplier<Collection<T>> supplier;

    public SchrodingerDeferredDomainCollection(@NonNull DomainContext context,
                                               @NonNull Supplier<Collection<T>> supplier) {
        super(context);
        this.supplier = supplier;
    }

    @Override
    protected Collection<T> doGetTarget() {
        Collection<T> collection = supplier.get();
        if (collection == null) {
            return Collections.emptyList();
        }
        return collection;
    }
}
