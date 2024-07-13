package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PropertiesConvertor;
import com.github.linyuzai.plugin.core.handle.extract.format.ObjectFormatter;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PropertiesMatcher;
import com.github.linyuzai.plugin.core.type.NestedType;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * {@link Properties} 提取器
 */
public abstract class PropertiesExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        if (type.toClass() == Properties.class) {
            return new PropertiesMatcher(annotations);
        }
        return null;
    }

    @Override
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        return new PropertiesConvertor();
    }

    @Override
    public PluginFormatter getFormatter(NestedType type, Annotation[] annotations) {
        if (type.toClass() == Properties.class) {
            //防止以Map解析
            return new ObjectFormatter();
        }
        return super.getFormatter(type, annotations);
    }

    /**
     * {@link Properties} 提取执行器器工厂
     */
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
