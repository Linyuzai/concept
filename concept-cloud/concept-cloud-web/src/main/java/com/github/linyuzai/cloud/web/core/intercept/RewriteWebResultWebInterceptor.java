package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.WebResult;

public abstract class RewriteWebResultWebInterceptor implements WebInterceptor {

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        context.put(WebResult.class, getWebResult(context));
        return returner.value(context);
    }

    protected abstract WebResult<?> getWebResult(WebContext context);

}
