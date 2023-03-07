package com.github.linyuzai.cloud.web.core.concept;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

@Getter
public class WebConceptImpl implements WebConcept {

    private final CloudWebProperties properties;

    private final WebInterceptorChainFactory chainFactory;

    private final List<WebInterceptor> requestInterceptors = new CopyOnWriteArrayList<>();

    private final List<WebInterceptor> responseInterceptors = new CopyOnWriteArrayList<>();

    private final List<WebInterceptor> errorInterceptors = new CopyOnWriteArrayList<>();

    public WebConceptImpl(CloudWebProperties properties,
                          WebInterceptorChainFactory chainFactory,
                          List<WebInterceptor> interceptors) {
        this.properties = properties;
        this.chainFactory = chainFactory;
        interceptors.forEach(this::addInterceptor);
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
    public void addInterceptor(WebInterceptor interceptor) {
        Set<WebInterceptor.Scope> scopes = interceptor.getScopes();
        if (scopes.contains(WebInterceptor.Scope.REQUEST)) {
            requestInterceptors.add(interceptor);
            requestInterceptors.sort(Comparator.comparingInt(WebInterceptor::getOrder));
        }
        if (scopes.contains(WebInterceptor.Scope.RESPONSE)) {
            responseInterceptors.add(interceptor);
            responseInterceptors.sort(Comparator.comparingInt(WebInterceptor::getOrder));
        }
        if (scopes.contains(WebInterceptor.Scope.ERROR)) {
            errorInterceptors.add(interceptor);
            errorInterceptors.sort(Comparator.comparingInt(WebInterceptor::getOrder));
        }
    }

    @Override
    public void removeInterceptor(WebInterceptor interceptor) {
        requestInterceptors.remove(interceptor);
        responseInterceptors.remove(interceptor);
        errorInterceptors.remove(interceptor);
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
