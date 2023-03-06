package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebError;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@RequiredArgsConstructor
@OnWebError
public class LoggerErrorInterceptor implements WebInterceptor {

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        Throwable e = context.get(Throwable.class);
        log.error(getMessage(context), e);
        return chain.next(context, returner);
    }

    protected String getMessage(WebContext context) {
        return context.get(WebContext.Request.PATH);
    }

    @Override
    public int getOrder() {
        return Order.LOGGER_ERROR;
    }
}
