package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.CloudWebException;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;

import java.lang.reflect.Method;
import java.util.Set;

public class BreakInterceptMethodWebInterceptorFactory implements MethodWebInterceptorFactory {

    @Override
    public boolean support(Set<WebInterceptor.Scope> scopes, Method method, Object bean) {
        return method.isAnnotationPresent(BreakIntercept.class);
    }

    @Override
    public WebInterceptor create(Set<WebInterceptor.Scope> scopes, Method method, Object bean) {
        if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) {
            BreakIntercept annotation = method.getAnnotation(BreakIntercept.class);
            return new AnnotationPredicateWebInterceptor(method, bean, scopes, annotation.useResponseBodyAsWebResult()).negate();
        } else {
            throw new CloudWebException("Return type must be boolean");
        }
    }

    @Override
    public int getOrder() {
        return MethodWebInterceptorFactory.super.getOrder() - 1;
    }
}
