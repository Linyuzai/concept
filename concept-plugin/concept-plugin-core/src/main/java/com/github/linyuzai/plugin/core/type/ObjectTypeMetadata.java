package com.github.linyuzai.plugin.core.type;

/**
 * 单个对象的 {@link TypeMetadata}
 */
public class ObjectTypeMetadata extends AbstractTypeMetadata {
    public ObjectTypeMetadata(Class<?> eClass) {
        setContainerType(eClass);
        setContainerClass(eClass);
        setElementType(eClass);
        setElementClass(eClass);
    }
}
