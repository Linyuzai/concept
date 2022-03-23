package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.match.PropertiesMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Properties;

public abstract class PropertiesExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        if (target == Properties.class || Map.class.isAssignableFrom(target)) {
            PluginConvertor convertor = getConvertorAdapter().adapt(metadata);
            return new PropertiesMatcher(target, annotations, convertor);
        }
        return null;
    }
}
