package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.proxy.AbstractDomainObjectProxy;
import lombok.*;

import java.lang.reflect.Method;

/**
 * 薛定谔模型代理
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerDomainObject extends AbstractDomainObjectProxy {

    protected final Class<? extends DomainObject> domainType;

    /**
     * 领域模型 id
     */
    @NonNull
    protected final String id;

    /**
     * 领域上下文
     */
    @NonNull
    protected final DomainContext context;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果是 getId 则直接返回
        if ("getId".equals(method.getName())) {
            return id;
        }
        return super.invoke(proxy, method, args);
    }

    /**
     * 获得被代理的对象
     */
    @Override
    public Object doGetTarget() {
        DomainRepository<?, ?> repository = context.get(getDomainRepositoryType());
        Object domain = repository.get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainType(), id);
        }
        return domain;
    }

    /**
     * 被代理的领域模型的存储
     */
    protected Class<? extends DomainRepository<?, ?>> getDomainRepositoryType() {
        return DomainLink.repository(getDomainType());
    }
}
