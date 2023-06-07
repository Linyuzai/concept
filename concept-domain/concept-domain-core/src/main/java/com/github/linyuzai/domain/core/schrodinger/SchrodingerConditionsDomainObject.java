package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import lombok.Getter;
import lombok.NonNull;

/**
 * 薛定谔模型代理
 */
@Getter
public class SchrodingerConditionsDomainObject<T extends DomainObject>
        extends AbstractSchrodingerDomainObject<T> implements DomainObject {

    @NonNull
    protected final Conditions conditions;

    public SchrodingerConditionsDomainObject(@NonNull DomainContext context,
                                             @NonNull Conditions conditions) {
        super(context);
        this.conditions = conditions;
    }

    /**
     * 获得被代理的对象
     */
    public T doGetTarget() {
        DomainCollection<T> select = getRepository().select(conditions);
        return list2one(getDomainObjectType(), select.list());
    }
}
