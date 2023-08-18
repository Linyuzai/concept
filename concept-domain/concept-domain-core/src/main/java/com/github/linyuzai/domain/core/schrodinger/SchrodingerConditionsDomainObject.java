package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import lombok.Getter;
import lombok.Setter;

/**
 * 薛定谔模型代理
 */
@Getter
@Setter
public class SchrodingerConditionsDomainObject<T extends DomainObject>
        extends AbstractSchrodingerDomainObject<T> implements DomainObject {

    protected Conditions conditions;

    /**
     * 获得被代理的对象
     */
    @Override
    protected T doGetTarget() {
        DomainCollection<T> select = getRepository().select(conditions);
        return list2one(getDomainObjectType(), select.list());
    }

    @Override
    protected void onRelease() {
        conditions = null;
    }
}
