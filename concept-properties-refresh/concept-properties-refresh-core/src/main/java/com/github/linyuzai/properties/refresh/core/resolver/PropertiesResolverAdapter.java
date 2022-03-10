package com.github.linyuzai.properties.refresh.core.resolver;

import java.lang.reflect.Type;

public interface PropertiesResolverAdapter {

    PropertiesResolver getResolver(String key, Type type);
}
