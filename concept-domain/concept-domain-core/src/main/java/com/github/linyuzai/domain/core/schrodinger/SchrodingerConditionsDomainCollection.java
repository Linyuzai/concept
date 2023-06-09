package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

/**
 * 薛定谔的集合模型
 */
@Getter
public class SchrodingerConditionsDomainCollection<T extends DomainObject>
        extends AbstractSchrodingerDomainCollection<T> implements DomainCollection<T> {

    @NonNull
    protected final Conditions conditions;

    public SchrodingerConditionsDomainCollection(@NonNull DomainContext context,
                                                 @NonNull Conditions conditions) {
        super(context);
        this.conditions = conditions;
    }

    @Override
    protected Collection<T> doGetTarget() {
        return getRepository().select(conditions).list();
    }
}
