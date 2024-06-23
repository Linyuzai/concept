package com.github.linyuzai.plugin.jar.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.DynamicExtractor;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginAnnotation;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClass;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
    public Invoker getInvoker(Method method, Parameter parameter) {
        Invoker invoker = super.getInvoker(method, parameter);
        if (invoker != null) {
            return invoker;
        }
        return getJarInvoker(method, parameter);
    }

    /**
     * 尝试匹配类和实例
     *
     * @param parameter 方法参数
     * @return 插件提取执行器或 null
     */
    protected Invoker getJarInvoker(Method method, Parameter parameter) {
        Invoker classInvoker = getClassInvoker(method, parameter);
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
    public Invoker getAnnotationInvoker(Method method, Parameter parameter, Annotation annotation) {
        if (annotation.annotationType() == PluginAnnotation.class ||
                annotation.annotationType() == PluginClass.class ||
                annotation.annotationType() == PluginClassName.class) {
            return getJarInvoker(method, parameter);
        }
        return super.getAnnotationInvoker(method, parameter, annotation);
    }

    /**
     * 尝试获得类提取器 {@link ClassExtractor} 对应的执行器
     *
     * @param parameter 方法参数
     * @return 类提取器 {@link ClassExtractor} 对应的执行器或 null
     */
    public Invoker getClassInvoker(Method method, Parameter parameter) {
        try {
            return new ClassExtractor<Void>() {

                @Override
                public void onExtract(Void plugin, PluginContext context) {

                }
            }.createInvoker(method, parameter);
        } catch (Throwable e) {
            return null;
        }
    }
}
