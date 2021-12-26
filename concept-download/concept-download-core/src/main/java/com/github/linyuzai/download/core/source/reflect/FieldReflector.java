package com.github.linyuzai.download.core.source.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
public class FieldReflector implements Reflector {

    private Field field;

    @Override
    public Object reflect(Object model) throws ReflectiveOperationException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(model);
    }
}
