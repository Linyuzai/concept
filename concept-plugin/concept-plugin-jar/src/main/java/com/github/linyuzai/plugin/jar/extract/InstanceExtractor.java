package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import com.github.linyuzai.plugin.jar.match.InstanceMatcher;

import java.lang.annotation.Annotation;

public abstract class InstanceExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Class<?> target, Annotation[] annotations) {
        if (metadata.isMap()) {
            return new InstanceMatcher.MapMatcher(metadata.getMapClass(), target, annotations);
        } else if (metadata.isList()) {
            return new InstanceMatcher.ListMatcher(metadata.getListClass(), target, annotations);
        } else if (metadata.isSet()) {
            return new InstanceMatcher.SetMatcher(metadata.getSetClass(), target, annotations);
        } else if (metadata.isCollection()) {
            return new InstanceMatcher.ListMatcher(metadata.getCollectionClass(), target, annotations);
        } else if (metadata.isArray()) {
            return new InstanceMatcher.ArrayMatcher(target, annotations);
        } else {
            return new InstanceMatcher.ObjectMatcher(target, annotations);
        }
    }
}
