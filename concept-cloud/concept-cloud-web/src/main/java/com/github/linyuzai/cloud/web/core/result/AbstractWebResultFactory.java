package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;

public abstract class AbstractWebResultFactory implements WebResultFactory {

    @Override
    public WebResult<?> create(WebContext context) {
        Throwable e = context.get(Throwable.class);
        Object body = context.get(WebContext.Response.BODY);
        if (e != null) {
            return createFailureWebResult(getFailureMessage(e, context), e, context);
        } else {
            if (body instanceof Throwable) {
                return createFailureWebResult(getFailureMessage((Throwable) body, context), (Throwable) body, context);
            } else {
                return createSuccessWebResult(getSuccessMessage(context), body, context);
            }
        }
    }

    protected String getSuccessMessage(WebContext context) {
        return null;
    }

    protected String getFailureMessage(Throwable e, WebContext context) {
        return e.getMessage();
    }

    protected abstract WebResult<?> createSuccessWebResult(String message, Object body, WebContext context);

    protected abstract WebResult<?> createFailureWebResult(String message, Throwable e, WebContext context);
}
