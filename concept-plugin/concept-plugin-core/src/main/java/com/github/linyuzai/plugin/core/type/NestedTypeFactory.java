package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;

public interface NestedTypeFactory {

    NestedType create(Type type);
}
