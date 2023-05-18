package com.bytedance.juejin.rpc;

import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import com.github.linyuzai.domain.core.condition.Conditions;

/**
 * 远程对象转换适配器
 *
 * @param <T>
 * @param <R>
 */
public interface RemoteObjectFacadeAdapter<T extends DomainObject, R extends Identifiable> {

    /**
     * 领域模型转远程对象
     */
    R do2ro(T object);

    /**
     * 远程对象转领域模型
     */
    T ro2do(R ro);

    /**
     * 条件转远程对象
     */
    default ConditionsRO conditions2ro(Conditions conditions) {
        return new ConditionsRO();
    }

    /**
     * 远程对象转条件
     */
    default Conditions ro2conditions(ConditionsRO ro) {
        return new Conditions();
    }
}
