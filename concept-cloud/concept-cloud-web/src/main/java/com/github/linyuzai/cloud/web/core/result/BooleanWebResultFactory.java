package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;

public class BooleanWebResultFactory extends AnnotationWebResultFactory {

    @Override
    protected WebResult<?> createSuccessWebResult(String message, Object body, WebContext context) {
        return new BooleanWebResult(true, message, body);
    }

    @Override
    protected WebResult<?> createFailureWebResult(String message, Throwable e, WebContext context) {
        return new BooleanWebResult(false, message, null);
    }
}
