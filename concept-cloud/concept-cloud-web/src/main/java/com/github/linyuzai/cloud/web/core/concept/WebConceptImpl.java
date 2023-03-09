package com.github.linyuzai.cloud.web.core.concept;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class WebConceptImpl implements WebConcept {

    private final CloudWebProperties properties;

    private final WebInterceptorChainFactory chainFactory;

    private final List<WebInterceptor> requestInterceptors = new CopyOnWriteArrayList<>();

    private final List<WebInterceptor> responseInterceptors = new CopyOnWriteArrayList<>();

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
    }

    @Override
    public void removeInterceptor(WebInterceptor interceptor) {
        requestInterceptors.remove(interceptor);
        responseInterceptors.remove(interceptor);
    }

    @Override
    public Object interceptRequest(WebContext context, ValueReturner returner, Object disableValue) {
        if (isRequestInterceptionEnabled()) {
            return chainFactory.create(0, requestInterceptors).next(context, returner);
        }
        return disableValue;
    }

    @Override
    public Object interceptResponse(WebContext context, ValueReturner returner, Object disableValue) {
        if (isResponseInterceptionEnabled()) {
            return chainFactory.create(0, responseInterceptors).next(context, returner);
        }
        return disableValue;
    }
}
