package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class AnnotationRewriteWebResultWebInterceptor extends RewriteWebResultWebInterceptor
        implements MethodInvocationWebInterceptor {

    private final Scope scope;

    private final Method method;

    private final Object bean;

    @SneakyThrows
    @Override
    protected WebResult<?> getWebResult(WebContext context) {
        return (WebResult<?>) method.invoke(bean, getArgs(context));
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
