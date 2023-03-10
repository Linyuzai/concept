package com.github.linyuzai.cloud.web.core.concept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;

public interface WebConcept {

    boolean isRequestInterceptionEnabled();

    boolean isResponseInterceptionEnabled();

    void addInterceptor(WebInterceptor interceptor);

    void removeInterceptor(WebInterceptor interceptor);

    Object interceptRequest(WebContext context, ValueReturner returner);

    Object interceptResponse(WebContext context, ValueReturner returner);
}
