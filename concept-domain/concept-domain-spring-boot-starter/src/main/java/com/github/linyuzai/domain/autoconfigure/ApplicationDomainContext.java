package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.DomainContext;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

/**
 * 基于 {@link ApplicationContext} 实现领域上下文
 */
@AllArgsConstructor
public class ApplicationDomainContext implements DomainContext {

    private ApplicationContext context;

    @Override
    public <T> T get(Class<T> type) {
        return context.getBean(type);
    }
}
