package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.Set;

public interface MethodWebInterceptorFactory extends Ordered {

    boolean support(Set<WebInterceptor.Scope> scopes, Method method, Object bean);

    WebInterceptor create(Set<WebInterceptor.Scope> scopes, Method method, Object bean);

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
