package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

/**
 * 异常打印拦截器
 */
@CommonsLog
@RequiredArgsConstructor
@OnResponse
public class LoggerErrorResponseInterceptor implements WebInterceptor {

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        Throwable e = context.get(Throwable.class);
        if (e != null) {
            log.error(getMessage(context), e);
        }
        return chain.next(context, returner);
    }

    /**
     * 请求的方法 + 路径
     */
    protected String getMessage(WebContext context) {
        return context.get(WebContext.Request.METHOD) + " " + context.get(WebContext.Request.PATH);
    }

    @Override
    public int getOrder() {
        return Orders.LOGGER_ERROR;
    }
}
