package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.format.*;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.*;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * 基于 {@link TypeMetadata} 的插件提取器
 *
 * @param <T> 插件类型
 */
@Setter
public abstract class TypeMetadataPluginExtractor<T> extends AbstractPluginExtractor<T> {

    /**
     * {@link TypeMetadata} 工厂
     */
    protected TypeMetadataFactory typeMetadataFactory;

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
        if (metadata.getElementClass() == null) {
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
        return getTypeMetadataFactory().create(type);
    }

    public TypeMetadataFactory getTypeMetadataFactory() {
        if (typeMetadataFactory == null) {
            typeMetadataFactory = new DefaultTypeMetadataFactory();
        }
        return typeMetadataFactory;
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
        if (metadata instanceof MapTypeMetadata) {
            return new MapToMapFormatter(metadata.getContainerClass());
        } else if (metadata instanceof ListTypeMetadata) {
            return new MapToListFormatter(metadata.getContainerClass());
        } else if (metadata instanceof SetTypeMetadata) {
            return new MapToSetFormatter(metadata.getContainerClass());
        } else if (metadata instanceof CollectionTypeMetadata) {
            return new MapToListFormatter(metadata.getContainerClass());
        } else if (metadata instanceof ArrayTypeMetadata) {
            return new MapToArrayFormatter(metadata.getContainerClass());
        } else if (metadata instanceof ObjectTypeMetadata) {
            return new MapToObjectFormatter();
        } else {
            return null;
        }
    }
}
