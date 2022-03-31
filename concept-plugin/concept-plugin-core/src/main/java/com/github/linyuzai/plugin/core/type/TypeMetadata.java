package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;

public interface TypeMetadata {

    Type getContainerType();

    Class<?> getContainerClass();

    Type getElementType();

    Class<?> getElementClass();
}
