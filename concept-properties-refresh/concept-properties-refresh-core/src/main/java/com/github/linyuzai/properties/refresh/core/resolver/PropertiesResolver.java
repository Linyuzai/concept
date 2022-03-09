package com.github.linyuzai.properties.refresh.core.resolver;

import java.lang.reflect.Type;

public interface PropertiesResolver {

    Object resolve(String key, Type type);
}
