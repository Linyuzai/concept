package com.github.linyuzai.plugin.autoconfigure.processor;

import com.github.linyuzai.plugin.autoconfigure.bean.BeanDynamicExtractor;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.extract.OnPluginExtract;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicPluginProcessor implements BeanPostProcessor,
        ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    /**
     * 方法拦截器信息
     */
    private final List<PluginMethods> pluginMethods = Collections.synchronizedList(new ArrayList<>());

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof AopInfrastructureBean) {
            // Ignore AOP infrastructure such as scoped proxies.
            return bean;
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (this.nonAnnotatedClasses.contains(targetClass)) {
            return bean;
        }

        Map<Method, Boolean> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<Boolean>) method ->
                        method.isAnnotationPresent(OnPluginExtract.class));
        if (annotatedMethods.isEmpty()) {
            this.nonAnnotatedClasses.add(targetClass);
        } else {
            List<Method> methods = new ArrayList<>();
            // Non-empty set of methods
            annotatedMethods.forEach((method, annotated) -> {
                if (annotated) {
                    methods.add(method);
                }
            });
            if (!methods.isEmpty()) {
                pluginMethods.add(new PluginMethods(bean, methods.toArray(new Method[0])));
            }
        }
        return bean;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.nonAnnotatedClasses.clear();
        if (this.pluginMethods.isEmpty()) {
            return;
        }
        PluginConcept concept = applicationContext.getBean(PluginConcept.class);
        List<PluginExtractor> extractors = new ArrayList<>();
        for (PluginMethods pms : pluginMethods) {
            extractors.add(new BeanDynamicExtractor(applicationContext, pms.target, pms.methods));
        }
        concept.addExtractors(extractors);
        pluginMethods.clear();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 方法拦截器信息
     */
    @RequiredArgsConstructor
    private static class PluginMethods {

        /**
         * 实例
         */
        final Object target;

        /**
         * 方法
         */
        final Method[] methods;
    }

}
