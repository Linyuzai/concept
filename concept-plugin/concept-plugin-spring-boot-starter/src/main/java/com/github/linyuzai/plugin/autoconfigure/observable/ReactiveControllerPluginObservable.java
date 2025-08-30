package com.github.linyuzai.plugin.autoconfigure.observable;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ReactiveControllerPluginObservable extends ControllerPluginObservable implements ApplicationContextAware {

    private RequestMappingHandlerMapping handlerMapping;

    private Method getMappingForMethod;

    @Override
    protected void doRegister(Class<?> type, Object controller, List<Runnable> destroyList) {
        Map<Method, RequestMappingInfo> methods = MethodIntrospector.selectMethods(type,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> {
                    try {
                        return (RequestMappingInfo)
                                getMappingForMethod.invoke(handlerMapping, method, type);
                    } catch (Throwable ex) {
                        throw new IllegalStateException("Invalid mapping on handler class [" +
                                type.getName() + "]: " + method, ex);
                    }
                });
        //BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(controller);
        methods.forEach((method, requestMappingInfo) -> {
            handlerMapping.registerMapping(requestMappingInfo, controller, method);
            destroyList.add(() -> handlerMapping.unregisterMapping(requestMappingInfo));
        });
    }

    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        this.handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        this.getMappingForMethod = RequestMappingHandlerMapping.class
                .getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
        this.getMappingForMethod.setAccessible(true);
    }
}
