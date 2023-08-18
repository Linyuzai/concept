package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * 薛定谔的集合模型
 */
@Getter
@Setter
public class SchrodingerIdentifiedDomainCollection<T extends DomainObject>
        extends AbstractSchrodingerDomainCollection<T> implements DomainCollection<T> {

    protected Collection<String> ids;

    @Override
    protected Collection<T> doGetTarget() {
        return getRepository().select(ids).list();
    }

    @Override
    protected void onRelease() {
        ids = null;
    }
}
