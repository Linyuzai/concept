package com.github.linyuzai.plugin.core.type;

public class ObjectTypeMetadata extends AbstractTypeMetadata {
    public ObjectTypeMetadata(Class<?> eClass) {
        setContainerType(eClass);
        setContainerClass(eClass);
        setElementType(eClass);
        setElementClass(eClass);
    }
}
