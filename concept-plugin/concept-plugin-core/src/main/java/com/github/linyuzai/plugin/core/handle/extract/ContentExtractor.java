package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.convert.ContentToByteArrayConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.ContentToInputStreamConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.ContentToStringConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.format.ObjectFormatter;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.ContentMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginText;
import com.github.linyuzai.plugin.core.type.NestedType;
import com.github.linyuzai.plugin.core.type.TypeMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;

/**
 * 插件内容提取器。
 * 支持 byte[] {@link String} {@link InputStream}。
 *
 * @param <T> 插件类型
 */
public abstract class ContentExtractor<T> extends AbstractPluginExtractor<T> {

    /**
     * 匹配类型为 byte[] {@link String} {@link InputStream}
     * 及对应类型的 {@link java.util.Collection} {@link java.util.List} {@link java.util.Set}
     * {@link java.util.Map} 和数组
     *
     * @param type        {@link TypeMetadata}
     * @param annotations 注解
     * @return {@link ContentMatcher}
     */
    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        if (String.class == cls || InputStream.class == cls || byte[].class == cls) {
            return new ContentMatcher(annotations);
        }
        return null;
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginConvertor}。
     * 特殊情况，如果是 {@link InputStream} 返回 {@link ContentToInputStreamConvertor}，
     * {@link String} 返回 {@link ContentToStringConvertor}。
     *
     * @param type        {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    @Override
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        if (String.class == cls) {
            Charset charset = getCharset(annotations);
            return new ContentToStringConvertor(charset);
        } else if (InputStream.class == cls) {
            return new ContentToInputStreamConvertor();
        } else if (byte[].class == cls) {
            return new ContentToByteArrayConvertor();
        } else {
            return null;
        }
    }

    protected Charset getCharset(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginText.class) {
                String charset = ((PluginText) annotation).charset();
                return Charset.forName(charset);
            }
        }
        return null;
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginFormatter}。
     * 特殊情况，如果是 byte[] 则返回 {@link ObjectFormatter}
     *
     * @param type        {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件格式器 {@link PluginFormatter}
     */
    @Override
    public PluginFormatter getFormatter(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        if (cls.isArray() && cls.getComponentType() == byte.class) {
            return new ObjectFormatter();
        }
        return super.getFormatter(type, annotations);
    }

    public static class InvokerFactory extends AbstractPluginExtractor.InvokerFactory {

        @Override
        protected AbstractPluginExtractor<?> createExtractor() {
            return new ContentExtractor<Object>() {
                @Override
                public void onExtract(Object plugin, PluginContext context) {
                }
            };
        }
    }
}
