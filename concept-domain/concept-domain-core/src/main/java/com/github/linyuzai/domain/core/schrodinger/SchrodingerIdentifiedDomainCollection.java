package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

/**
 * 薛定谔的集合模型
 */
@Getter
public class SchrodingerIdentifiedDomainCollection<T extends DomainObject>
        extends AbstractSchrodingerDomainCollection<T> implements DomainCollection<T> {

    @NonNull
    protected final Collection<String> ids;

    public SchrodingerIdentifiedDomainCollection(@NonNull DomainContext context,
                                                 @NonNull Collection<String> ids) {
        super(context);
        this.ids = ids;
    }

    @Override
    protected Collection<T> doGetTarget() {
        return getRepository().select(ids).list();
    }
}
