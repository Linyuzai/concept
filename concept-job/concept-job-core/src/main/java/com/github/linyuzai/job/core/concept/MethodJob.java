package com.github.linyuzai.job.core.concept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public class MethodJob extends AbstractJob {

    private final Object target;

    private final Method method;

    @Override
    public Object run(String params) throws Throwable {
        Class<?>[] types = method.getParameterTypes();
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if (String.class.isAssignableFrom(type)) {
                args[i] = params;
            }
        }
        return method.invoke(target, args);
    }
}
