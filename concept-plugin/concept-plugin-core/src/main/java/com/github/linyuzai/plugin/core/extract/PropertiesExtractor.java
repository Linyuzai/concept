package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.convert.PropertiesToMapMapConvertor;
import com.github.linyuzai.plugin.core.format.MapToObjectFormatter;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.match.PropertiesMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

public abstract class PropertiesExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        if (target == Properties.class || Map.class.isAssignableFrom(target)
                || metadata.isMap() && target == String.class) {
            return new PropertiesMatcher(annotations);
        }
        return null;
    }

    @Override
    public TypeMetadata createTypeMetadata(Type type) {
        if (type == Properties.class) {
            TypeMetadata metadata = new TypeMetadata();
            metadata.setTargetType(Properties.class);
            metadata.setTargetClass(Properties.class);
            return metadata;
        }
        return super.createTypeMetadata(type);
    }

    @Override
    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        if (target != Properties.class && Map.class.isAssignableFrom(target)) {
            return new PropertiesToMapMapConvertor(target);
        }
        return super.getConvertor(metadata, annotations);
    }

    @Override
    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        if (metadata.isMap() && target == String.class) {
            return new MapToObjectFormatter();
        }
        return super.getFormatter(metadata, annotations);
    }
}
