package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;

/**
 * 单个对象的 {@link TypeMetadata}
 */
public class ObjectTypeMetadata extends AbstractTypeMetadata {

    public ObjectTypeMetadata(Type eType, Class<?> eClass) {
        setContainerType(eType);
        setContainerClass(eClass);
        setElementType(eType);
        setElementClass(eClass);
    }
}
