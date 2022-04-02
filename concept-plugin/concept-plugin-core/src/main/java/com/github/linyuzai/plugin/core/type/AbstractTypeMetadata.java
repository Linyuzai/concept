package com.github.linyuzai.plugin.core.type;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

/**
 * {@link TypeMetadata} 抽象类
 */
@Getter
@Setter
public abstract class AbstractTypeMetadata implements TypeMetadata {

    /**
     * 容器类的 {@link Type}
     */
    private Type containerType;

    /**
     * 容器类的 {@link Class}
     */
    private Class<?> containerClass;

    /**
     * 元素类的 {@link Type}
     */
    private Type elementType;

    /**
     * 元素类的 {@link Class}
     */
    private Class<?> elementClass;
}
