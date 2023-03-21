package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 方法拦截器工厂
 */
public interface MethodWebInterceptorFactory extends Ordered {

    /**
     * 是否支持该方法
     *
     * @param scopes 作用域
     * @param method 方法
     * @param bean   实例
     * @return true 支持，false 不支持
     */
    boolean support(Set<WebInterceptor.Scope> scopes, Method method, Object bean);

    /**
     * 生成拦截器
     *
     * @param scopes 作用域
     * @param method 方法
     * @param bean   实例
     * @return 拦截器
     */
    WebInterceptor create(Set<WebInterceptor.Scope> scopes, Method method, Object bean);

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
