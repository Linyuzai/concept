package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

@Getter
class AnnotationPredicateWebInterceptor extends PredicateWebInterceptor implements MethodInvocationWebInterceptor {

    private final Scope scope;

    private final Method method;

    public AnnotationPredicateWebInterceptor(Scope scope, Method method, Object bean) {
        this.scope = scope;
        this.method = method;
        setPredicate(new AnnotationPredicate(method, bean));
    }

    @Override
    public Set<Scope> getScopes() {
        return Collections.singleton(scope);
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
