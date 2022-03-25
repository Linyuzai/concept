package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import com.github.linyuzai.plugin.jar.match.ClassMatcher;

import java.lang.annotation.Annotation;

public abstract class ClassExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        return new ClassMatcher(target, annotations);
    }

    @Override
    public boolean isClassTypeMetadata() {
        return true;
    }
}
