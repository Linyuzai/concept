package com.github.linyuzai.plugin.core.type;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
public abstract class AbstractTypeMetadata implements TypeMetadata {

    private Type containerType;

    private Class<?> containerClass;

    private Type elementType;

    private Class<?> elementClass;
}
