package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.AbstractPluginExtractor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.NestedType;

import java.lang.annotation.Annotation;

/**
 * Bean 提取器
 */
public abstract class BeanExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        return new BeanMatcher(type.toClass(), annotations);
    }

    @Override
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        return new BeanConvertor();
    }

    /**
     * Bean 插件提取执行器工厂
     */
    public static class InvokerFactory extends AbstractPluginExtractor.InvokerFactory {

        @Override
        protected AbstractPluginExtractor<?> createExtractor() {
            return new BeanExtractor<Object>() {
                @Override
                public void onExtract(Object plugin, PluginContext context) {
                }
            };
        }
    }
}
