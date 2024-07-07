package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PropertiesConvertor;
import com.github.linyuzai.plugin.core.handle.extract.format.ObjectFormatter;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.ContentMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PropertiesMatcher;
import com.github.linyuzai.plugin.core.type.NestedType;
import com.github.linyuzai.plugin.core.type.TypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Properties} 文件提取器。
 * 支持 {@link Properties} {@link Map}。
 *
 * @param <T> 插件类型
 */
public abstract class PropertiesExtractor<T> extends AbstractPluginExtractor<T> {

    /**
     * 匹配类型为 {@link Properties} {@link Map}
     * 及对应类型的 {@link java.util.Collection} {@link java.util.List} {@link java.util.Set}
     * {@link java.util.Map} 和数组
     *
     * @param type        {@link TypeMetadata}
     * @param annotations 注解
     * @return {@link ContentMatcher}
     */
    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        if (type.toClass() == Properties.class) {
            return new PropertiesMatcher(annotations);
        }
        return null;
    }

    /**
     * 根据 {@link TypeMetadata} 和注解获得 {@link PluginConvertor}。
     *
     * @param type    {@link TypeMetadata}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    @Override
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        return new PropertiesConvertor();
    }

    @Override
    public PluginFormatter getFormatter(NestedType type, Annotation[] annotations) {
        if (type.toClass() == Properties.class) {
            return new ObjectFormatter();
        }
        return super.getFormatter(type, annotations);
    }

    public static class InvokerFactory extends AbstractPluginExtractor.InvokerFactory {

        @Override
        protected AbstractPluginExtractor<?> createExtractor() {
            return new PropertiesExtractor<Object>() {
                @Override
                public void onExtract(Object plugin, PluginContext context) {
                }
            };
        }
    }
}
