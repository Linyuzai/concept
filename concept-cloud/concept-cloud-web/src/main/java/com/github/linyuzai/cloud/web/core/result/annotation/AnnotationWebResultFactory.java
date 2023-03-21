package com.github.linyuzai.cloud.web.core.result.annotation;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.AbstractWebResultFactory;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 结合 {@link ResultMessage} 获得信息的返回值包装工厂实现
 */
public abstract class AnnotationWebResultFactory extends AbstractWebResultFactory {

    /**
     * 获得成功信息
     *
     * @param context 上下文
     * @return 如果标注了 {@link ResultMessage} 注解并且获得的成功信息不为 null 则返回该信息，否则调用父类方法获得成功信息
     */
    @Override
    protected String getSuccessMessage(WebContext context) {
        String message = getMessage(context, ResultMessage::success);
        if (message != null) {
            return message;
        }
        return super.getSuccessMessage(context);
    }

    /**
     * 获得失败信息
     *
     * @param e       异常
     * @param context 上下文
     * @return 如果标注了 {@link ResultMessage} 注解并且获得的失败信息不为 null 则返回该信息，否则调用父类方法获得失败信息
     */
    @Override
    protected String getFailureMessage(Throwable e, WebContext context) {
        String message = getMessage(context, ResultMessage::failure);
        if (message != null) {
            return message;
        }
        return super.getFailureMessage(e, context);
    }

    /**
     * 结合 {@link ResultMessage} 注解获得信息
     *
     * @param context  上下文
     * @param function 获得成功信息或失败信息
     * @return 如果标注了 {@link ResultMessage} 注解并且注解方法返回不为空则返回注解上标注的信息，否则返回 null
     */
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

    /**
     * 获得方法上标注的 {@link ResultMessage} 注解
     *
     * @param context 上下文
     * @return 如果方法上标注了 {@link ResultMessage} 注解则返回对应的注解，否则返回 null
     */
    protected ResultMessage getAnnotation(WebContext context) {
        MethodParameter parameter = context.get(MethodParameter.class);
        if (parameter == null) {
            return null;
        }
        Method method = parameter.getMethod();
        if (method == null) {
            return null;
        }
        return method.getAnnotation(ResultMessage.class);
    }
}
