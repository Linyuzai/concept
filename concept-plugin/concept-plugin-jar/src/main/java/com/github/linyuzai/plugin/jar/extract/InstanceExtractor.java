package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.convert.*;
import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import com.github.linyuzai.plugin.jar.match.InstanceMatcher;

import java.lang.annotation.Annotation;

public abstract class InstanceExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        PluginConvertor convertor = getConvertorAdapter().adapt(metadata);
        return new InstanceMatcher(target, annotations, convertor);
    }
}
