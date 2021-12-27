package com.github.linyuzai.download.core.source.reflect;

import lombok.Getter;

import java.lang.reflect.Field;

/**
 * 基于属性的反射器 / Field based reflector
 */
@Getter
public class FieldReflector implements Reflector {

    private final Field field;

    public FieldReflector(Field field) {
        this.field = field;
        if (!this.field.isAccessible()) {
            this.field.setAccessible(true);
        }
    }

    @Override
    public Object reflect(Object model) throws ReflectiveOperationException {
        return field.get(model);
    }
}
