package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.TypeMetadata;
import com.github.linyuzai.plugin.jar.match.InstanceMatcher;

import java.lang.annotation.Annotation;

/**
 * 实例提取器
 *
 * @param <T> 实例类型
 */
public abstract class InstanceExtractor<T> extends TypeMetadataPluginExtractor<T> {

    /**
     * 返回一个 {@link InstanceMatcher}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return {@link InstanceMatcher}
     */
    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> elementClass = metadata.getElementClass();
        return new InstanceMatcher(elementClass, annotations);
    }
}
