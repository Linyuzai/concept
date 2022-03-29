package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.format.*;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * 基于 {@link TypeMetadata} 的插件提取器
 *
 * @param <T> 插件类型
 */
public abstract class TypeMetadataPluginExtractor<T> extends AbstractPluginExtractor<T> {

    /**
     * 尝试根据可用的 {@link TypeMetadata} 获得 {@link PluginMatcher}
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件匹配器 {@link PluginMatcher}
     */
    @Override
    public PluginMatcher getMatcher(Type type, Annotation[] annotations) {
        TypeMetadata metadata = getAvailableTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        return getMatcher(metadata, annotations);
    }

    /**
     * 尝试根据可用的 {@link TypeMetadata} 获得 {@link PluginConvertor}
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    @Override
    public PluginConvertor getConvertor(Type type, Annotation[] annotations) {
        TypeMetadata metadata = getAvailableTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        return getConvertor(metadata, annotations);
    }

    /**
     * 尝试根据可用的 {@link TypeMetadata} 获得 {@link PluginFormatter}
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件格式器 {@link PluginFormatter}
     */
    @Override
    public PluginFormatter getFormatter(Type type, Annotation[] annotations) {
        TypeMetadata metadata = getAvailableTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        return getFormatter(metadata, annotations);
    }

    /**
     * 获得可用的 {@link TypeMetadata}
     *
     * @param type 插件类型 {@link Type}
     * @return 如果 {@link TypeMetadata} 可用则返回该对象，否则返回 null
     */
    public TypeMetadata getAvailableTypeMetadata(Type type) {
        TypeMetadata metadata = createTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        if (metadata.getTargetClass() == null) {
            return null;
        }
        return metadata;
    }

    /**
     * 创建 {@link TypeMetadata}
     *
     * @param type 插件类型 {@link Type}
     * @return {@link TypeMetadata} 或 null
     */
    public TypeMetadata createTypeMetadata(Type type) {
        return TypeMetadata.create(type);
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginMatcher}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件匹配器 {@link PluginMatcher}
     */
    public abstract PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations);

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginConvertor}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        return null;
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginFormatter}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件格式器 {@link PluginFormatter}
     */
    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
        if (metadata.isMap()) {
            return new MapToMapFormatter(metadata.getMapClass());
        } else if (metadata.isList()) {
            return new MapToListFormatter(metadata.getListClass());
        } else if (metadata.isSet()) {
            return new MapToSetFormatter(metadata.getSetClass());
        } else if (metadata.isCollection()) {
            return new MapToListFormatter(metadata.getCollectionClass());
        } else if (metadata.isArray()) {
            return new MapToArrayFormatter(metadata.getArrayClass());
        } else {
            return new MapToObjectFormatter();
        }
    }
}
