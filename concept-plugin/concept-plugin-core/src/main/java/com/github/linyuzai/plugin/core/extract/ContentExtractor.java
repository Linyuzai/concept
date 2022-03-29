package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.ByteArrayToInputStreamMapConvertor;
import com.github.linyuzai.plugin.core.convert.ByteArrayToStringMapConvertor;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.format.MapToObjectFormatter;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.match.ContentMatcher;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

/**
 * 插件内容提取器。
 * 支持 byte[] {@link String} {@link InputStream}
 *
 * @param <T> 插件类型
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class ContentExtractor<T> extends TypeMetadataPluginExtractor<T> {

    /**
     * 提取成 {@link String} 时的编码
     */
    private Charset charset;

    public ContentExtractor(String charset) {
        this.charset = Charset.forName(charset);
    }

    /**
     * 匹配类型为 byte[] {@link String} {@link InputStream}
     * 及对应类型的 {@link java.util.Collection} {@link java.util.List} {@link java.util.Set}
     * {@link java.util.Map} 和数组
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return {@link ContentMatcher}
     */
    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        if (metadata.isArray() && target == byte.class ||
                target == byte[].class ||
                target == String.class ||
                InputStream.class.isAssignableFrom(target)) {
            return new ContentMatcher(annotations);
        }
        return null;
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginConvertor}。
     * 特殊情况，如果是 {@link InputStream} 返回 {@link ByteArrayToInputStreamMapConvertor}，
     * {@link String} 返回 {@link ByteArrayToStringMapConvertor}。
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    @Override
    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        if (InputStream.class == target) {
            return new ByteArrayToInputStreamMapConvertor();
        }
        if (String.class == target) {
            return new ByteArrayToStringMapConvertor(charset);
        }
        return super.getConvertor(metadata, annotations);
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginFormatter}。
     * 特殊情况，如果是 byte[] 则返回 {@link MapToObjectFormatter}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件格式器 {@link PluginFormatter}
     */
    @Override
    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
        if (metadata.isArray() && metadata.getTargetClass() == byte.class) {
            return new MapToObjectFormatter();
        }
        return super.getFormatter(metadata, annotations);
    }
}
