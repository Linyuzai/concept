package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.AbstractPluginExtractor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.NestedType;

import java.lang.annotation.Annotation;

/**
 * 实例提取器
 *
 * @param <T> 实例类型
 */
public abstract class BeanExtractor<T> extends AbstractPluginExtractor<T> {

    /**
     * 返回一个 {@link BeanMatcher}
     *
     * @param type
     * @param annotations 注解
     * @return {@link BeanMatcher}
     */
    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        return new BeanMatcher(type.toClass(), annotations);
    }

    @Override
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        return new BeanConvertor();
    }

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
