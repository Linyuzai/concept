package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.PredicateWebInterceptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Predicate;

@Getter
public class AnnotationPredicateWebInterceptor extends PredicateWebInterceptor implements MethodInvocationSupport {

    private final Method method;

    public AnnotationPredicateWebInterceptor(Method method, Object bean, Set<Scope> scopes, boolean useResponseBodyAsWebResult) {
        this.method = method;
        setScopes(scopes);
        setUseResponseBodyAsWebResult(useResponseBodyAsWebResult);
        setPredicate(new AnnotationPredicate(method, bean));
    }

    @Override
    public int getOrder() {
        return getMethodOrder(Orders.PREDICATE);
    }

    @RequiredArgsConstructor
    private class AnnotationPredicate implements Predicate<WebContext> {

        private final Method method;

        private final Object bean;

        @SneakyThrows
        @Override
        public boolean test(WebContext context) {
            Object[] args = getArgs(context);
            return (boolean) method.invoke(bean, args);
        }
    }
}
