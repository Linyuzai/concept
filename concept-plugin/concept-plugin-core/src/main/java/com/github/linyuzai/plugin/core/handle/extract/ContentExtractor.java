package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.handle.extract.convert.ContentToInputStreamConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.ContentToStringConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.format.ObjectFormatter;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.ContentMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.ArrayTypeMetadata;
import com.github.linyuzai.plugin.core.type.TypeMetadata;
import lombok.*;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

/**
 * 插件内容提取器。
 * 支持 byte[] {@link String} {@link InputStream}。
 *
 * @param <T> 插件类型
 */
@Getter
@RequiredArgsConstructor
public abstract class ContentExtractor<T> extends TypeMetadataPluginExtractor<T> {

    /**
     * 提取成 {@link String} 时的编码
     */
    private final Charset charset;

    public ContentExtractor() {
        charset = null;
    }

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
        Class<?> elementClass = metadata.getElementClass();
        if (metadata instanceof ArrayTypeMetadata && elementClass == byte.class ||
                elementClass == byte[].class ||
                elementClass == String.class ||
                InputStream.class.isAssignableFrom(elementClass)) {
            return new ContentMatcher(annotations);
        }
        return null;
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginConvertor}。
     * 特殊情况，如果是 {@link InputStream} 返回 {@link ContentToInputStreamConvertor}，
     * {@link String} 返回 {@link ContentToStringConvertor}。
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    @Override
    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> elementClass = metadata.getElementClass();
        if (InputStream.class == elementClass) {
            return new ContentToInputStreamConvertor();
        }
        if (String.class == elementClass) {
            return new ContentToStringConvertor(charset);
        }
        return super.getConvertor(metadata, annotations);
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginFormatter}。
     * 特殊情况，如果是 byte[] 则返回 {@link ObjectFormatter}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件格式器 {@link PluginFormatter}
     */
    @Override
    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
        if (metadata instanceof ArrayTypeMetadata && metadata.getElementClass() == byte.class) {
            return new ObjectFormatter();
        }
        return super.getFormatter(metadata, annotations);
    }
}
