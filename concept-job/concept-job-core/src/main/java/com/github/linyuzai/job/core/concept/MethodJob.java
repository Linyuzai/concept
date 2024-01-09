package com.github.linyuzai.job.core.concept;

import com.github.linyuzai.job.core.context.JobContext;
import com.github.linyuzai.job.core.context.JobContextImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class MethodJob extends AbstractJob {

    private final Object target;

    private final Method method;

    @Override
    public Object run(Map<String, String> params) throws Throwable {
        Class<?>[] types = method.getParameterTypes();
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if (JobContext.class.isAssignableFrom(type)) {
                JobContext context = new JobContextImpl(params);
                args[i] = context;
            }
            if (Map.class.isAssignableFrom(type)) {
                args[i] = params;
            }
        }
        return method.invoke(target, args);
    }
}
