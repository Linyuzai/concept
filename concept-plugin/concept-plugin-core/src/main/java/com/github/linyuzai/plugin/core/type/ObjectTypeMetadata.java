package com.github.linyuzai.plugin.core.type;

/**
 * 单个对象的 {@link TypeMetadata}
 */
public class ObjectTypeMetadata extends AbstractTypeMetadata {

    public static ObjectTypeMetadata of(Class<?> c) {
        ObjectTypeMetadata metadata = new ObjectTypeMetadata();
        metadata.setContainerType(c);
        metadata.setContainerClass(c);
        metadata.setElementType(c);
        metadata.setElementClass(c);
        return metadata;
    }
}
