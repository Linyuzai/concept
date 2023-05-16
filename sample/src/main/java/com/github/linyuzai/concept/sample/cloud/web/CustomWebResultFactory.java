package com.github.linyuzai.concept.sample.cloud.web;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.annotation.AnnotationWebResultFactory;
import com.github.linyuzai.cloud.web.core.result.LongWebResult;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import org.springframework.stereotype.Component;

//@Component
public class CustomWebResultFactory extends AnnotationWebResultFactory {

    @Override
    protected WebResult<?> createSuccessWebResult(String message, Object body, WebContext context) {
        return new LongWebResult(0L, message, body);
    }

    @Override
    protected WebResult<?> createFailureWebResult(String message, Throwable e, WebContext context) {
        return new LongWebResult(-1L, message, null);
    }
}
