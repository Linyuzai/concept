package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PropertiesToMapConvertor;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.handle.extract.format.AbstractPluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.format.ObjectFormatter;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.ContentMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PropertiesMatcher;
import com.github.linyuzai.plugin.core.type.MapTypeMetadata;
import com.github.linyuzai.plugin.core.type.ObjectTypeMetadata;
import com.github.linyuzai.plugin.core.type.TypeMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * {@link Properties} 文件提取器。
 * 支持 {@link Properties} {@link Map}。
 *
 * @param <T> 插件类型
 */
public abstract class PropertiesExtractor<T> extends TypeMetadataPluginExtractor<T> {

    /**
     * 匹配类型为 {@link Properties} {@link Map}
     * 及对应类型的 {@link java.util.Collection} {@link java.util.List} {@link java.util.Set}
     * {@link java.util.Map} 和数组
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return {@link ContentMatcher}
     */
    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> elementClass = metadata.getElementClass();
        if (elementClass == Properties.class || Map.class.isAssignableFrom(elementClass)
                || metadata instanceof MapTypeMetadata && elementClass == String.class) {
            return new PropertiesMatcher(annotations);
        }
        /*for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginProperties.class) {
                return new PropertiesMatcher(annotations);
            }
        }*/
        return null;
    }

    /**
     * 根据 {@link Type} 获得 {@link TypeMetadata}。
     * 特殊情况，如果是 {@link Properties} 由于实现了 {@link Map} 所以需要特殊处理。
     *
     * @param type {@link Type}
     * @return {@link TypeMetadata}
     */
    @Override
    public TypeMetadata createTypeMetadata(Type type) {
        if (type == Properties.class) {
            return ObjectTypeMetadata.of(Properties.class);
        }
        return super.createTypeMetadata(type);
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginConvertor}。
     * 特殊情况，如果是 {@link Properties} 或 {@link Map} 返回 {@link PropertiesToMapConvertor}，
     * {@link String} 并且是一个 {@link Map} 则返回 {@link PropertiesToMapConvertor} 作为单个配置的类型。
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    @Override
    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> elementClass = metadata.getElementClass();
        if (elementClass != Properties.class && Map.class.isAssignableFrom(elementClass)) {
            return new PropertiesToMapConvertor(elementClass);
        }
        if (metadata instanceof MapTypeMetadata && elementClass == String.class) {
            return new PropertiesToMapConvertor(metadata.getContainerClass());
        }
        return super.getConvertor(metadata, annotations);
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginFormatter}。
     * 特殊情况，如果是 {@link String} 并且是一个 {@link Map} 则返回 {@link ObjectFormatter}，
     * {@link String} 并且是一个 {@link Object} 则返回 {@link PropertiesFormatter}，
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件格式器 {@link PluginFormatter}
     */
    @Override
    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> elementClass = metadata.getElementClass();
        if (metadata instanceof MapTypeMetadata && elementClass == String.class) {
            return new ObjectFormatter();
        }
        if (metadata instanceof ObjectTypeMetadata && elementClass == String.class) {
            return new PropertiesFormatter();
        }
        return super.getFormatter(metadata, annotations);
    }

    /**
     * 用于提取单个属性值的格式器
     */
    public static class PropertiesFormatter extends AbstractPluginFormatter<Map<?, Properties>, String> {

        @Override
        public String doFormat(Map<?, Properties> source) {
            String value = null;
            for (Properties properties : source.values()) {
                Set<String> propertyNames = properties.stringPropertyNames();
                for (String propertyName : propertyNames) {
                    String property = properties.getProperty(propertyName);
                    if (value == null) {
                        value = property;
                    } else {
                        throw new PluginException("More than one property matched: " + source);
                    }
                }
            }
            return value;
        }
    }
}
