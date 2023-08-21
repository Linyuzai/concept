package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import lombok.Getter;
import lombok.Setter;

/**
 * 薛定谔的集合模型
 */
@Getter
@Setter
public class SchrodingerConditionsDomainCollection<T extends DomainObject>
        extends AbstractSchrodingerDomainCollection<T> implements DomainCollection<T> {

    protected Conditions conditions;

    @Override
    protected DomainCollection<T> doGetTarget() {
        return getRepository().select(conditions);
    }

    @Override
    protected void onRelease() {
        conditions = null;
    }
}
