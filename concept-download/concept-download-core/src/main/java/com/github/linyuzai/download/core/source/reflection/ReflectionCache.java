package com.github.linyuzai.download.core.source.reflection;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ReflectionCache {

    private final Map<Class<? extends Annotation>, Method> methodMap = new HashMap<>();

    private final Map<Class<? extends Annotation>, Field> fieldMap = new HashMap<>();

    public Object reflect(Class<? extends Annotation> clazz, Object target) throws ReflectiveOperationException {
        Method method = methodMap.get(clazz);
        if (method == null) {
            Field field = fieldMap.get(clazz);
            if (field == null) {
                return null;
            } else {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(target);
            }
        } else {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(target);
        }
    }
}
