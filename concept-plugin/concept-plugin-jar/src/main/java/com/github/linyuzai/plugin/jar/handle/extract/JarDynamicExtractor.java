package com.github.linyuzai.plugin.jar.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.DynamicExtractor;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginAnnotation;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClass;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * 基于 jar 的动态插件提取器
 */
public class JarDynamicExtractor extends DynamicExtractor {

    public JarDynamicExtractor(Object target) {
        super(target);
    }

    public JarDynamicExtractor(Object target, Method... methods) {
        super(target, methods);
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
        return getJarInvoker(parameter);
    }

    /**
     * 尝试匹配类和实例
     *
     * @param parameter 方法参数
     * @return 插件提取执行器或 null
     */
    protected Invoker getJarInvoker(Parameter parameter) {
        Invoker classInvoker = getClassInvoker(parameter);
        if (classInvoker != null) {
            return classInvoker;
        }
        return null;
    }

    /**
     * 额外匹配类相关的执行器。
     * {@link ClassExtractor} 对应的执行器。
     *
     * @param annotation 注解
     * @param parameter  参数 {@link Parameter}
     * @return 插件提取执行器
     */
    @Override
    public Invoker getAnnotationInvoker(Annotation annotation, Parameter parameter) {
        if (annotation.annotationType() == PluginAnnotation.class ||
                annotation.annotationType() == PluginClass.class ||
                annotation.annotationType() == PluginClassName.class) {
            return getJarInvoker(parameter);
        }
        return super.getAnnotationInvoker(annotation, parameter);
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
                public void onExtract(Void plugin, PluginContext context) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }
}
