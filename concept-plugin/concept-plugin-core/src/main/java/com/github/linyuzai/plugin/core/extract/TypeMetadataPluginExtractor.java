package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class TypeMetadataPluginExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(Type type, Annotation[] annotations) {
        TypeMetadata metadata = TypeMetadata.from(type);
        if (metadata == null) {
            return null;
        }
        return getMatcher(metadata, type, annotations);
    }

    public abstract PluginMatcher getMatcher(TypeMetadata metadata, Type type, Annotation[] annotations);
}
