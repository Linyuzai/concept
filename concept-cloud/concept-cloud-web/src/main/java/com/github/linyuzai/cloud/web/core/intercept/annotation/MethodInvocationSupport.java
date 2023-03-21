package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 方法调用相关支持
 */
public interface MethodInvocationSupport {

    Method getMethod();

    /**
     * 匹配方法入参
     *
     * @param context 上下文
     * @return 方法入参
     */
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

    /**
     * 获得方法拦截排序值
     *
     * @param defaultOrder 默认的排序值
     * @return 排序值
     */
    default int getMethodOrder(int defaultOrder) {
        Method method = getMethod();
        Order annotation = method.getAnnotation(Order.class);
        if (annotation != null) {
            return annotation.value();
        }
        return defaultOrder;
    }
}
