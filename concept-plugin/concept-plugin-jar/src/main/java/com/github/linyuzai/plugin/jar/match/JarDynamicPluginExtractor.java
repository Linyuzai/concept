package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.extract.DynamicPluginExtractor;
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
    public Invoker getInvoker(Parameter parameter) {
        Invoker invoker = super.getInvoker(parameter);
        if (invoker != null) {
            return invoker;
        }
        return getInvoker0(parameter);
    }

    private Invoker getInvoker0(Parameter parameter) {
        Invoker classInvoker = getClassInvoker(parameter);
        if (classInvoker != null) {
            return classInvoker;
        }
        Invoker instanceInvoker = getInstanceInvoker(parameter);
        if (instanceInvoker != null) {
            return instanceInvoker;
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
    public Invoker getAssociationInvoker(Annotation annotation, Parameter parameter) {
        if (annotation.annotationType() == PluginAnnotation.class ||
                annotation.annotationType() == PluginClass.class ||
                annotation.annotationType() == PluginClassName.class ||
                annotation.annotationType() == PluginPackage.class) {
            return getInvoker0(parameter);
        }
        return super.getAssociationInvoker(annotation, parameter);
    }

    public Invoker getClassInvoker(Parameter parameter) {
        try {
            return new ClassExtractor<Void>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Void plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    public Invoker getInstanceInvoker(Parameter parameter) {
        try {
            return new InstanceExtractor<Void>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Void plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }
}
