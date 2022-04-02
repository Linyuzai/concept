package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;

/**
 * 一般通过泛型解析获得。
 * 指定了容器类 {@link java.util.Map} {@link java.util.List} {@link java.util.Set} {@link java.util.Collection} 和数组等，
 * 还有里面的元素类型。
 */
public interface TypeMetadata {

    /**
     * 获得容器类的 {@link Type}
     *
     * @return 容器类的 {@link Type}
     */
    Type getContainerType();

    /**
     * 获得容器类的 {@link Class}
     *
     * @return 容器类的 {@link Class}
     */
    Class<?> getContainerClass();

    /**
     * 获得元素类的 {@link Type}
     *
     * @return 元素类的 {@link Type}
     */
    Type getElementType();

    /**
     * 获得元素类的 {@link Class}
     *
     * @return 元素类的 {@link Class}
     */
    Class<?> getElementClass();
}
