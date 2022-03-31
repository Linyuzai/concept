package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.ArrayTypeMetadata;
import com.github.linyuzai.plugin.core.type.DefaultTypeMetadataFactory;
import com.github.linyuzai.plugin.core.type.TypeMetadata;
import com.github.linyuzai.plugin.core.type.TypeMetadataFactory;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import com.github.linyuzai.plugin.jar.match.ClassMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public abstract class ClassExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> elementClass = metadata.getElementClass();
        return new ClassMatcher(elementClass, annotations);
    }

    @Override
    public TypeMetadataFactory getTypeMetadataFactory() {
        if (typeMetadataFactory == null) {
            typeMetadataFactory = new DefaultTypeMetadataFactory() {

                @Override
                public TypeMetadata create(Type type) {
                    TypeMetadata metadata = super.create(type);
                    if (metadata instanceof ArrayTypeMetadata) {
                        ((ArrayTypeMetadata) metadata).setElementClass(Class.class);
                    }
                    //metadata.setTargetClass(getTargetClass(metadata.getElementType()));
                    return metadata;
                }

                @Override
                public Class<?> getElementClass(Type type) {
                    if (type instanceof Class) {
                        if (type == Class.class) {
                            return Object.class;
                        }
                    } else if (type instanceof ParameterizedType) {
                        Type rawType = ((ParameterizedType) type).getRawType();
                        if (rawType instanceof Class) {
                            if (rawType == Class.class) {
                                Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                                return ReflectionUtils.toClass(arguments[0]);
                            }
                        }
                    } else if (type instanceof WildcardType) {
                        Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                        if (upperBounds.length > 0) {
                            Type upperBound = upperBounds[0];
                            if (upperBound instanceof Class || upperBound instanceof ParameterizedType) {
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
