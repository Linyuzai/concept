package com.github.linyuzai.plugin.jar.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.AbstractPluginExtractor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.NestedType;
import com.github.linyuzai.plugin.jar.handle.extract.convert.ClassConvertor;
import com.github.linyuzai.plugin.jar.handle.extract.match.ClassMatcher;

import java.lang.annotation.Annotation;

/**
 * 类提取器
 */
public abstract class ClassExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        if (type.toClass() == Class.class) {
            return new ClassMatcher(type.getChildren().get(0).toClass(), annotations);
        }
        return null;
    }

    @Override
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        return new ClassConvertor();
    }

    /**
     * 类插件提取执行器工厂
     */
    public static class InvokerFactory extends AbstractPluginExtractor.InvokerFactory {

        @Override
        protected AbstractPluginExtractor<?> createExtractor() {
            return new ClassExtractor<Object>() {
                @Override
                public void onExtract(Object plugin, PluginContext context) {
                }
            };
        }
    }
}
