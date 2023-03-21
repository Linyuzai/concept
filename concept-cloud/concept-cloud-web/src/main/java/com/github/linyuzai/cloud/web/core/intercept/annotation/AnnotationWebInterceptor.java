package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class AnnotationWebInterceptor implements WebInterceptor, MethodInvocationSupport {

    private final Method method;

    private final Object bean;

    private final Set<Scope> scopes;

    @SneakyThrows
    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        Object[] args = getArgs(context);
        method.invoke(bean, args);
        return chain.next(context, returner);
    }

    @Override
    public Set<Scope> getScopes() {
        return scopes;
    }

    @Override
    public int getOrder() {
        return getMethodOrder(Ordered.LOWEST_PRECEDENCE);
    }
}
