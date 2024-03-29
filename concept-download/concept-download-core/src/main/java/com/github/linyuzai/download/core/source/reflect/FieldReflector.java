package com.github.linyuzai.download.core.source.reflect;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * 基于 {@link Field} 的 {@link Reflector}。
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

    @SneakyThrows
    @Override
    public Object reflect(Object model) {
        return field.get(model);
    }
}
