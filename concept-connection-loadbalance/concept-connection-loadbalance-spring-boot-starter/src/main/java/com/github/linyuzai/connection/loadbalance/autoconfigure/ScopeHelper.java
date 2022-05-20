package com.github.linyuzai.connection.loadbalance.autoconfigure;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ScopeHelper {

    private final GenericApplicationContext context;

    private final boolean ignoreScope;

    public ScopeHelper(GenericApplicationContext context, List<ScopeName> sns) {
        this.context = context;
        this.ignoreScope = sns.size() <= 1;
    }

    @SafeVarargs
    public final <T> T getBean(Class<T> clazz, Class<? extends Annotation>... scopes) {
        if (ignoreScope) {
            return context.getBean(clazz);
        }
        for (Class<? extends Annotation> scope : scopes) {
            List<String> names = new ArrayList<>();
            List<T> beans = new ArrayList<>();
            Scope annotation = AnnotationUtils.findAnnotation(scope, Scope.class);
            if (annotation == null) {
                continue;
            }
            String[] beanNames = context.getBeanNamesForType(clazz);
            for (String beanName : beanNames) {
                BeanDefinition definition = context.getBeanDefinition(beanName);
                if (annotation.value().equals(definition.getScope())) {
                    try {
                        T bean = context.getBean(beanName, clazz);
                        names.add(beanName);
                        beans.add(bean);
                    } catch (Throwable ignore) {
                    }
                }
            }
            int size = beans.size();
            if (size == 1) {
                return beans.get(0);
            }
            if (size > 1) {
                throw new NoUniqueBeanDefinitionException(clazz, names);
            }
        }
        throw new NoSuchBeanDefinitionException(clazz);
    }

    @SafeVarargs
    public final <T> List<T> getBeans(Class<T> clazz, Class<? extends Annotation>... scopes) {
        if (ignoreScope) {
            return new ArrayList<>(context.getBeansOfType(clazz).values());
        }
        List<T> beans = new ArrayList<>();
        for (Class<? extends Annotation> scope : scopes) {
            Scope annotation = AnnotationUtils.findAnnotation(scope, Scope.class);
            if (annotation == null) {
                continue;
            }
            String[] beanNames = context.getBeanNamesForType(clazz);
            for (String beanName : beanNames) {
                BeanDefinition definition = context.getBeanDefinition(beanName);
                if (annotation.value().equals(definition.getScope())) {
                    T bean = context.getBean(beanName, clazz);
                    beans.add(bean);
                }
            }
        }
        return beans;
    }
}
