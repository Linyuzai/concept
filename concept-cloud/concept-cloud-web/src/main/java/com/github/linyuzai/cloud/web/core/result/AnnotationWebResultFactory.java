package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.annotation.ResultMessage;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.function.Function;

public abstract class AnnotationWebResultFactory extends AbstractWebResultFactory {

    @Override
    protected String getSuccessMessage(WebContext context) {
        String message = getMessage(context, ResultMessage::success);
        if (message != null) {
            return message;
        }
        return super.getSuccessMessage(context);
    }

    @Override
    protected String getFailureMessage(Throwable e, WebContext context) {
        String message = getMessage(context, ResultMessage::failure);
        if (message != null) {
            return message;
        }
        return super.getFailureMessage(e, context);
    }

    protected String getMessage(WebContext context, Function<ResultMessage, String> function) {
        ResultMessage annotation = getAnnotation(context);
        if (annotation == null) {
            return null;
        }
        String message = function.apply(annotation);
        if (message.isEmpty()) {
            return null;
        } else {
            return message;
        }
    }

    protected ResultMessage getAnnotation(WebContext context) {
        MethodParameter parameter = context.get(MethodParameter.class);
        Method method = parameter.getMethod();
        if (method == null) {
            return null;
        }
        return method.getAnnotation(ResultMessage.class);
    }
}
