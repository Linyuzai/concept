package com.github.linyuzai.properties.refresh.core.resolver;

import com.github.linyuzai.properties.refresh.core.KeyTypePair;

import java.lang.reflect.Type;

public interface PropertiesResolver {

    Object resolve(String key,Type type);
}
