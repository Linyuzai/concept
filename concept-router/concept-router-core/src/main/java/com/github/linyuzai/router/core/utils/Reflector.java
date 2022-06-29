package com.github.linyuzai.router.core.utils;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * 未用到
 */
@AllArgsConstructor
public class Reflector {

    public final Class<?> target;

    public Reflector(String className) throws ClassNotFoundException {
        this(Class.forName(className));
    }

    @SneakyThrows
    public Field field(Class<?> fieldClass) {
        for (Field field : target.getDeclaredFields()) {
            if (field.getType() == fieldClass) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new NoSuchFieldException(fieldClass.getName());
    }
}
