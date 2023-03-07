package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface MethodInvocationWebInterceptor extends WebInterceptor {

    Method getMethod();

    default Object[] getArgs(WebContext context) {
        Method method = getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            if (type == WebContext.class) {
                args[i] = context;
            } else {
                args[i] = context.get(type);
            }
        }
        return args;
    }

    default int getMethodOrder(int defaultOrder) {
        Method method = getMethod();
        Order annotation = method.getAnnotation(Order.class);
        if (annotation != null) {
            return annotation.value();
        }
        return defaultOrder;
    }
}
