package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.extract.DynamicPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.jar.extract.ClassExtractor;
import com.github.linyuzai.plugin.jar.extract.InstanceExtractor;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class JarDynamicPluginExtractor extends DynamicPluginExtractor {

    public JarDynamicPluginExtractor(@NonNull Object target) {
        super(target);
    }

    @Override
    public PluginMatcher getMatcher(Parameter parameter) {
        PluginMatcher matcher = super.getMatcher(parameter);
        if (matcher != null) {
            return matcher;
        }
        return getMatcher0(parameter);
    }

    private PluginMatcher getMatcher0(Parameter parameter) {
        PluginMatcher classMatcher = getClassMatcher(parameter);
        if (classMatcher != null) {
            return classMatcher;
        }
        PluginMatcher instanceMatcher = getInstanceMatcher(parameter);
        if (instanceMatcher != null) {
            return instanceMatcher;
        }
        return null;
    }

    @Override
    public boolean hasAssociationAnnotation(Annotation annotation) {
        if (annotation.annotationType() == PluginAnnotation.class ||
                annotation.annotationType() == PluginClass.class ||
                annotation.annotationType() == PluginClassName.class ||
                annotation.annotationType() == PluginPackage.class) {
            return true;
        }
        return super.hasAssociationAnnotation(annotation);
    }

    @Override
    public PluginMatcher getAssociationMatcher(Annotation annotation, Parameter parameter) {
        if (annotation.annotationType() == PluginAnnotation.class ||
                annotation.annotationType() == PluginClass.class ||
                annotation.annotationType() == PluginClassName.class ||
                annotation.annotationType() == PluginPackage.class) {
            return getMatcher0(parameter);
        }
        return super.getAssociationMatcher(annotation, parameter);
    }

    public PluginMatcher getClassMatcher(Parameter parameter) {
        return new ClassExtractor<Object>() {

            @Override
            public void match(Type type, Annotation[] annotations) {
                matcher = getMatcher(parameter.getParameterizedType(), parameter.getAnnotations());
            }

            @Override
            public void onExtract(Object plugin) {

            }
        }.getMatcher();
    }

    public PluginMatcher getInstanceMatcher(Parameter parameter) {
        return new InstanceExtractor<Object>() {

            @Override
            public void match(Type type, Annotation[] annotations) {
                matcher = getMatcher(parameter.getParameterizedType(), parameter.getAnnotations());
            }

            @Override
            public void onExtract(Object plugin) {

            }
        }.getMatcher();
    }
}
