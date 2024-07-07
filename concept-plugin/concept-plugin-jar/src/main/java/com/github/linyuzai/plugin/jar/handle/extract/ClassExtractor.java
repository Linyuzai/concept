package com.github.linyuzai.plugin.jar.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.AbstractPluginExtractor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.*;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import com.github.linyuzai.plugin.jar.handle.extract.convert.ClassConvertor;
import com.github.linyuzai.plugin.jar.handle.extract.match.ClassMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * 类提取器
 *
 * @param <T> 类型
 */
public abstract class ClassExtractor<T> extends AbstractPluginExtractor<T> {

    /**
     * 返回一个 {@link ClassMatcher}
     *
     * @param type        {@link TypeMetadata}
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

    @Deprecated
    public static class ClassTypeMetadataFactory extends DefaultTypeMetadataFactory {

        @Override
        public Class<?> getElementClass(Type type) {
            if (type instanceof Class) {
                //Class
                if (type == Class.class) {
                    return Object.class;
                }
            } else if (type instanceof ParameterizedType) {
                //Class<A>
                Type rawType = ((ParameterizedType) type).getRawType();
                if (rawType instanceof Class) {
                    if (rawType == Class.class) {
                        Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                        //获得A
                        return ReflectionUtils.toClass(arguments[0]);
                    }
                }
            } else if (type instanceof WildcardType) {
                //? extends Class<B>
                Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length > 0) {
                    Type upperBound = upperBounds[0];
                    if (upperBound instanceof Class || upperBound instanceof ParameterizedType) {
                        //获得Class<B>
                        return getElementClass(upperBound);
                    }
                }
            }
            return null;
        }
    }
}
