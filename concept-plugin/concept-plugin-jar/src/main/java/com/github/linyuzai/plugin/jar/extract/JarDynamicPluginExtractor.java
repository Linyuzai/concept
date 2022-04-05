package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.DynamicPluginExtractor;
import com.github.linyuzai.plugin.jar.match.PluginAnnotation;
import com.github.linyuzai.plugin.jar.match.PluginClass;
import com.github.linyuzai.plugin.jar.match.PluginClassName;
import com.github.linyuzai.plugin.jar.match.PluginPackage;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * 基于 jar 的动态插件提取器
 */
public class JarDynamicPluginExtractor extends DynamicPluginExtractor {

    public JarDynamicPluginExtractor(@NonNull Object target) {
        super(target);
    }

    /**
     * 额外匹配类和实例
     *
     * @param parameter 方法参数 {@link Parameter}
     * @return 插件提取执行器
     */
    @Override
    public Invoker getInvoker(Parameter parameter) {
        Invoker invoker = super.getInvoker(parameter);
        if (invoker != null) {
            return invoker;
        }
        return getInvoker0(parameter);
    }

    /**
     * 尝试匹配类和实例
     *
     * @param parameter 方法参数
     * @return 插件提取执行器或 null
     */
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

    /**
     * 额外匹配类相关的指定注解
     *
     * @param annotation 注解
     * @return 如果是明确指定的返回 true，否则返回 false
     */
    @Override
    public boolean hasExplicitAnnotation(Annotation annotation) {
        if (annotation.annotationType() == PluginAnnotation.class ||
                annotation.annotationType() == PluginClass.class ||
                annotation.annotationType() == PluginClassName.class ||
                annotation.annotationType() == PluginPackage.class) {
            return true;
        }
        return super.hasExplicitAnnotation(annotation);
    }

    /**
     * 额外匹配类相关的执行器。
     * {@link ClassExtractor} {@link InstanceExtractor} 对应的执行器。
     *
     * @param annotation 注解
     * @param parameter  参数 {@link Parameter}
     * @return 插件提取执行器
     */
    @Override
    public Invoker getExplicitInvoker(Annotation annotation, Parameter parameter) {
        if (annotation.annotationType() == PluginAnnotation.class ||
                annotation.annotationType() == PluginClass.class ||
                annotation.annotationType() == PluginClassName.class ||
                annotation.annotationType() == PluginPackage.class) {
            return getInvoker0(parameter);
        }
        return super.getExplicitInvoker(annotation, parameter);
    }

    /**
     * 尝试获得类提取器 {@link ClassExtractor} 对应的执行器
     *
     * @param parameter 方法参数
     * @return 类提取器 {@link ClassExtractor} 对应的执行器或 null
     */
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

    /**
     * 尝试获得类提取器 {@link InstanceExtractor} 对应的执行器
     *
     * @param parameter 方法参数
     * @return 类提取器 {@link InstanceExtractor} 对应的执行器或 null
     */
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
