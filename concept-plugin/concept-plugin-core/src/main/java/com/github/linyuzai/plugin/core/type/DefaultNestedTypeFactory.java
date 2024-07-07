package com.github.linyuzai.plugin.core.type;

import lombok.Getter;

import java.lang.reflect.Type;

public class DefaultNestedTypeFactory implements NestedTypeFactory {

    @Getter
    private static final DefaultNestedTypeFactory instance = new DefaultNestedTypeFactory();

    @Override
    public NestedType create(Type type) {
        return new DefaultNestedType(type);
    }
}
