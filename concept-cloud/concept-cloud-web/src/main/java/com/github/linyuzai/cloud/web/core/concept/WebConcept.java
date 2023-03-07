package com.github.linyuzai.cloud.web.core.concept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;

import java.util.Set;
import java.util.function.Supplier;

public interface WebConcept {

    boolean isRequestInterceptionEnabled();

    boolean isResponseInterceptionEnabled();

    boolean isErrorInterceptionEnabled();

    void addInterceptor(WebInterceptor interceptor);

    void removeInterceptor(WebInterceptor interceptor);

    Object interceptRequest(Supplier<WebContext> supplier, Object defaultValue);

    Object interceptResponse(Supplier<WebContext> supplier, Object defaultValue);

    Object interceptError(Supplier<WebContext> supplier, Object defaultValue);
}
