package com.github.linyuzai.download.core.source.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class MethodReflector implements Reflector {

    private Method method;

    @Override
    public Object reflect(Object model) throws ReflectiveOperationException {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(model);
    }
}
