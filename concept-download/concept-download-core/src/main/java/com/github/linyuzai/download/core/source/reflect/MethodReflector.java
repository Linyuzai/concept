package com.github.linyuzai.download.core.source.reflect;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * ๅบไบ {@link Method} ็ {@link Reflector}ใ
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
