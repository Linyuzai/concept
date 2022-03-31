package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;

public interface TypeMetadataFactory {

    TypeMetadata create(Type type);
}
