package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.CloudWebException;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 中断拦截的方法拦截器工厂
 */
public class BreakInterceptMethodWebInterceptorFactory implements MethodWebInterceptorFactory {

    /**
     * 支持标注了 {@link BreakIntercept} 注解的方法
     *
     * @param scopes 作用域
     * @param method 方法
     * @param bean   实例
     * @return true 支持，false 不支持
     */
    @Override
    public boolean support(Set<WebInterceptor.Scope> scopes, Method method, Object bean) {
        return method.isAnnotationPresent(BreakIntercept.class);
    }

    @Override
    public WebInterceptor create(Set<WebInterceptor.Scope> scopes, Method method, Object bean) {
        //返回值必须为 boolean 作为断言结果
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
