package com.github.linyuzai.download.core.source.reflect;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class MethodReflector implements Reflector {

    private final Method method;

    public MethodReflector(Method method) {
        this.method = method;
        if (!this.method.isAccessible()) {
            this.method.setAccessible(true);
        }
    }

    @Override
    public Object reflect(Object model) throws ReflectiveOperationException {
        return method.invoke(model);
    }
}
