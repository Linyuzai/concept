package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;

/**
 * {@link TypeMetadata} 工厂
 */
public interface TypeMetadataFactory {

    /**
     * 创建 {@link TypeMetadata}
     *
     * @param type 泛型 {@link Type}
     * @return {@link TypeMetadata} 实例或 null
     */
    TypeMetadata create(Type type);
}
