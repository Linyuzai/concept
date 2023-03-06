package com.github.linyuzai.cloud.web.core.concept;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.Getter;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
public class WebConceptImpl implements WebConcept {

    private final CloudWebProperties properties;

    private final WebInterceptorChainFactory chainFactory;

    private final List<WebInterceptor> requestInterceptors;

    private final List<WebInterceptor> responseInterceptors;

    private final List<WebInterceptor> errorInterceptors;

    public WebConceptImpl(CloudWebProperties properties,
                          WebInterceptorChainFactory chainFactory,
                          List<WebInterceptor> interceptors) {
        this.properties = properties;
        this.chainFactory = chainFactory;
        this.requestInterceptors = interceptors.stream()
                .filter(it -> it.getScopes().contains(WebInterceptor.Scope.REQUEST))
                .collect(Collectors.toList());
        this.responseInterceptors = interceptors.stream()
                .filter(it -> it.getScopes().contains(WebInterceptor.Scope.RESPONSE))
                .collect(Collectors.toList());
        this.errorInterceptors = interceptors.stream()
                .filter(it -> it.getScopes().contains(WebInterceptor.Scope.ERROR))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isRequestInterceptionEnabled() {
        return properties.getIntercept().getRequest().isEnabled();
    }

    @Override
    public boolean isResponseInterceptionEnabled() {
        return properties.getIntercept().getResponse().isEnabled();
    }

    @Override
    public boolean isErrorInterceptionEnabled() {
        return properties.getIntercept().getError().isEnabled();
    }

    @Override
    public Object interceptRequest(Supplier<WebContext> supplier, Object disableValue) {
        if (isRequestInterceptionEnabled()) {
            WebContext context = supplier.get();
            return chainFactory.create(requestInterceptors).next(context, ctx -> null);
        }
        return disableValue;
    }

    @Override
    public Object interceptResponse(Supplier<WebContext> supplier, Object disableValue) {
        if (isResponseInterceptionEnabled()) {
            WebContext context = supplier.get();
            return chainFactory.create(responseInterceptors).next(context, ctx -> ctx.get(WebResult.class));
        }
        return disableValue;
    }

    @Override
    public Object interceptError(Supplier<WebContext> supplier, Object disableValue) {
        if (isErrorInterceptionEnabled()) {
            WebContext context = supplier.get();
            return chainFactory.create(errorInterceptors).next(context, ctx -> ctx.get(Throwable.class));
        }
        return disableValue;
    }
}
