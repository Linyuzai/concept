package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class AnnotationWebInterceptor implements MethodInvocationWebInterceptor {

    private final Scope scope;

    private final Method method;

    private final Object bean;

    @SneakyThrows
    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        Object[] args = getArgs(context);
        method.invoke(bean, args);
        return chain.next(context, returner);
    }

    @Override
    public Set<Scope> getScopes() {
        return Collections.singleton(scope);
    }

    @Override
    public int getOrder() {
        return getMethodOrder(Ordered.LOWEST_PRECEDENCE);
    }
}
