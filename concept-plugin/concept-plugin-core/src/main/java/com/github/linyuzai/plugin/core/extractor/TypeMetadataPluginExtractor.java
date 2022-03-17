package com.github.linyuzai.plugin.core.extractor;

import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.reflect.Type;

public abstract class TypeMetadataPluginExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher bind(Type type) {
        TypeMetadata metadata = TypeMetadata.from(type);
        if (metadata == null) {
            return null;
        }
        return bind(metadata, type);
    }

    public abstract PluginMatcher bind(TypeMetadata metadata, Type type);
}
