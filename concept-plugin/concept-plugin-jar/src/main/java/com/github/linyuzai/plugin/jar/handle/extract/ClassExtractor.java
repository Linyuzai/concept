package com.github.linyuzai.plugin.jar.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.AbstractPluginExtractor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.*;
import com.github.linyuzai.plugin.jar.handle.extract.convert.ClassConvertor;
import com.github.linyuzai.plugin.jar.handle.extract.match.ClassMatcher;

import java.lang.annotation.Annotation;

/**
 * 类提取器
 *
 * @param <T> 类型
 */
public abstract class ClassExtractor<T> extends AbstractPluginExtractor<T> {

    /**
     * 返回一个 {@link ClassMatcher}
     *
     * @param type
     * @param annotations 注解
     * @return {@link ClassMatcher}
     */
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
