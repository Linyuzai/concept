package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.DefaultTypeMetadataFactory;
import com.github.linyuzai.plugin.core.type.ObjectTypeMetadata;
import com.github.linyuzai.plugin.core.type.TypeMetadata;
import com.github.linyuzai.plugin.core.type.TypeMetadataFactory;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import com.github.linyuzai.plugin.jar.match.ClassMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * 类提取器
 *
 * @param <T> 类型
 */
public abstract class ClassExtractor<T> extends TypeMetadataPluginExtractor<T> {

    /**
     * 返回一个 {@link ClassMatcher}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return {@link ClassMatcher}
     */
    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        if (metadata instanceof ObjectTypeMetadata) {
            if (metadata.getContainerClass() != Class.class) {
                return null;
            }
        }
        Class<?> elementClass = metadata.getElementClass();
        return new ClassMatcher(elementClass, annotations);
    }

    /**
     * 由于类的元素对象需要获得其泛型类型，所以重写了相关方法
     *
     * @return {@link TypeMetadataFactory}
     */
    @Override
    public TypeMetadataFactory getTypeMetadataFactory() {
        if (typeMetadataFactory == null) {
            typeMetadataFactory = new DefaultTypeMetadataFactory() {

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
            };
        }
        return typeMetadataFactory;
    }
}
