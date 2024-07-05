package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.JarDynamicExtractor;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Getter
public class BeanDynamicExtractor extends JarDynamicExtractor {

    private ApplicationContext applicationContext;

    protected BeanDynamicExtractor() {
    }

    public BeanDynamicExtractor(ApplicationContext applicationContext, Object target) {
        this(applicationContext, target, getPluginMethod(target));
    }

    public BeanDynamicExtractor(ApplicationContext applicationContext, Object target, Method... methods) {
        this.target = target;
        this.methods = methods;
        this.applicationContext = applicationContext;
        createInvokers();
    }

    @Override
    public Invoker getAnnotationInvoker(Method method, Parameter parameter, Annotation annotation) {
        if (annotation.annotationType() == PluginBean.class) {
            return getBeanInvoker(method, parameter);
        }
        return super.getAnnotationInvoker(method, parameter, annotation);
    }

    public Invoker getBeanInvoker(Method method, Parameter parameter) {
        try {
            return new BeanExtractor<Void>(applicationContext) {

                @Override
                public void onExtract(Void plugin, PluginContext context) {

                }
            }.createInvoker(method, parameter);
        } catch (Throwable e) {
            return null;
        }
    }
}
