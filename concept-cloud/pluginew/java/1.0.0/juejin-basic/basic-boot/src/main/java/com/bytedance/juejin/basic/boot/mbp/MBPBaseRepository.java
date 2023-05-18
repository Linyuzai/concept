package com.bytedance.juejin.basic.boot.mbp;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import com.github.linyuzai.domain.mbp.MBPDomainRepository;

/**
 * 基于 MBP 的基础仓储
 * <p>
 * 方便扩展
 */
public abstract class MBPBaseRepository<T extends DomainObject, C extends DomainCollection<T>, P extends Identifiable>
        extends MBPDomainRepository<T, C, P> {

}
