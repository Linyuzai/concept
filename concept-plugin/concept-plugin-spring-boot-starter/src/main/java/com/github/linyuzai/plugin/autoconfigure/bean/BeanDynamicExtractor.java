package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.JarDynamicExtractor;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
    public void useDefaultInvokerFactories() {
        super.useDefaultInvokerFactories();
        addInvokerFactory(new BeanExtractor.InvokerFactory(applicationContext));
    }

    /*@Override
    public Invoker getAnnotationInvoker(Method method, Parameter parameter, Annotation annotation) {
        if (annotation.annotationType() == PluginBean.class) {
            return getBeanInvoker(method, parameter);
        }
        return super.getAnnotationInvoker(method, parameter, annotation);
    }*/

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
