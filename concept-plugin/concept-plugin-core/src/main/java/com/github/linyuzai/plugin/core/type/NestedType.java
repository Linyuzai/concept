package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;
import java.util.List;

public interface NestedType {

    Type toType();

    Class<?> toClass();

    NestedType getParent();

    List<NestedType> getChildren();
}
