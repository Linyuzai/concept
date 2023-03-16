package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;

import java.lang.reflect.Method;
import java.util.Set;

public class SampleMethodWebInterceptorFactory implements MethodWebInterceptorFactory {

    @Override
    public boolean support(Set<WebInterceptor.Scope> scopes, Method method, Object bean) {
        return true;
    }

    @Override
    public WebInterceptor create(Set<WebInterceptor.Scope> scopes, Method method, Object bean) {
        return new AnnotationWebInterceptor(method, bean, scopes);
    }
}
