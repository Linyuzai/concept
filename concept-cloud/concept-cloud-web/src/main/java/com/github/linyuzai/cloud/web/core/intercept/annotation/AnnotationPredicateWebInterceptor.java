package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.PredicateWebInterceptor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 注解断言拦截器
 */
@Getter
public class AnnotationPredicateWebInterceptor extends PredicateWebInterceptor implements MethodInvocationSupport {

    private final Method method;

    private final Object bean;

    public AnnotationPredicateWebInterceptor(Method method, Object bean, Set<Scope> scopes, boolean useResponseBodyAsWebResult) {
        this.method = method;
        this.bean = bean;
        setScopes(scopes);
        setUseResponseBodyAsWebResult(useResponseBodyAsWebResult);
        setPredicate(new AnnotationPredicate());
    }

    @Override
    public int getOrder() {
        return getMethodOrder(Orders.PREDICATE);
    }

    private class AnnotationPredicate implements Predicate<WebContext> {

        @SneakyThrows
        @Override
        public boolean test(WebContext context) {
            Object[] args = getArgs(context);
            //方法返回值作为断言结果
            return (boolean) method.invoke(bean, args);
        }
    }
}
