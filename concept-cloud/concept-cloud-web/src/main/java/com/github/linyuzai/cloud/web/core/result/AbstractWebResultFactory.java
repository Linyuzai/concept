package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;

public abstract class AbstractWebResultFactory implements WebResultFactory {

    @Override
    public WebResult<?> create(WebContext context) {
        Throwable e = context.get(Throwable.class);
        if (e == null) {
            Object body = context.get(WebContext.Response.BODY);
            return createSuccessWebResult(getSuccessMessage(context), body, context);
        } else {
            return createFailureWebResult(getFailureMessage(e, context), e, context);
        }
    }

    protected String getSuccessMessage(WebContext context) {
        return null;
    }

    protected String getFailureMessage(Throwable e, WebContext context) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

    protected abstract WebResult<?> createSuccessWebResult(String message, Object body, WebContext context);

    protected abstract WebResult<?> createFailureWebResult(String message, Throwable e, WebContext context);
}
