package com.github.linyuzai.download.core.source.reflect;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * 基于 {@link Method} 的 {@link Reflector}。
 */
@Getter
public class MethodReflector implements Reflector {

    private final Method method;

    public MethodReflector(Method method) {
        this.method = method;
        if (!this.method.isAccessible()) {
            this.method.setAccessible(true);
        }
    }

    @SneakyThrows
    @Override
    public Object reflect(Object model) {
        return method.invoke(model);
    }
}
