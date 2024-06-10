package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.jar.extract.JarDynamicExtractor;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

@Getter
public class BeanDynamicExtractor extends JarDynamicExtractor {

    private final ApplicationContext applicationContext;

    public BeanDynamicExtractor(ApplicationContext applicationContext, Object target) {
        super(target);
        this.applicationContext = applicationContext;
    }

    public BeanDynamicExtractor(ApplicationContext applicationContext, Object target, Method... methods) {
        super(target, methods);
        this.applicationContext = applicationContext;
    }

    @Override
    public Invoker getAnnotationInvoker(Annotation annotation, Parameter parameter) {
        if (annotation.annotationType() == PluginBean.class) {
            return getBeanInvoker(parameter);
        }
        return super.getAnnotationInvoker(annotation, parameter);
    }

    public Invoker getBeanInvoker(Parameter parameter) {
        try {
            return new BeanExtractor<Void>(applicationContext) {

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
