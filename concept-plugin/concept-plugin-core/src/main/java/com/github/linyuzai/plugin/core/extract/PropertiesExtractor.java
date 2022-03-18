package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.match.PropertiesMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Properties;

public abstract class PropertiesExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Class<?> target, Annotation[] annotations) {
        if (target == Properties.class || Map.class.isAssignableFrom(target)) {
            if (metadata.isMap()) {
                return new PropertiesMatcher.MapMatcher(metadata.getMapClass(), target, annotations);
            } else if (metadata.isList()) {
                return new PropertiesMatcher.ListMatcher(metadata.getListClass(), target, annotations);
            } else if (metadata.isSet()) {
                return new PropertiesMatcher.SetMatcher(metadata.getSetClass(), target, annotations);
            } else if (metadata.isCollection()) {
                return new PropertiesMatcher.ListMatcher(metadata.getCollectionClass(), target, annotations);
            } else if (metadata.isArray()) {
                return new PropertiesMatcher.ArrayMatcher(target, annotations);
            } else {
                return new PropertiesMatcher.ObjectMatcher(target, annotations);
            }
        }
        return null;
    }
}
