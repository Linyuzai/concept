package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.CloudWebException;
import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebError;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class WebInterceptorAnnotationBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware, SmartInitializingSingleton {

    private WebConcept concept;

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof AopInfrastructureBean || bean instanceof WebInterceptor) {
            // Ignore AOP infrastructure such as scoped proxies.
            return bean;
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (this.nonAnnotatedClasses.contains(targetClass)) {
            return bean;
        }
        if (AnnotationUtils.isCandidateClass(targetClass,
                Arrays.asList(OnWebRequest.class, OnWebResponse.class, OnWebError.class))) {
            Map<Method, List<InterceptMetadata>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<List<InterceptMetadata>>) method -> {
                        List<InterceptMetadata> list = new ArrayList<>();
                        OnWebRequest onWebRequest = method.getAnnotation(OnWebRequest.class);
                        if (onWebRequest != null) {
                            list.add(new InterceptMetadata(WebInterceptor.Scope.REQUEST, onWebRequest.value()));
                        }
                        OnWebResponse onWebResponse = method.getAnnotation(OnWebResponse.class);
                        if (onWebResponse != null) {
                            list.add(new InterceptMetadata(WebInterceptor.Scope.RESPONSE, onWebResponse.value()));
                        }
                        OnWebError onWebError = method.getAnnotation(OnWebError.class);
                        if (onWebError != null) {
                            list.add(new InterceptMetadata(WebInterceptor.Scope.ERROR, onWebError.value()));
                        }
                        return list;
                    });
            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(targetClass);
            } else {
                // Non-empty set of methods
                annotatedMethods.forEach((method, scopes) -> processWebInterceptor(scopes, method, bean));
            }
        }
        return bean;
    }

    private void processWebInterceptor(List<InterceptMetadata> metadataList, Method method, Object bean) {
        if (metadataList.isEmpty()) {
            return;
        }
        method.setAccessible(true);
        for (InterceptMetadata metadata : metadataList) {
            //这里可以优化成适配器
            if (metadata.type == Predicate.class) {
                //断言拦截器
                if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) {
                    PredicateWebInterceptor interceptor =
                            new AnnotationPredicateWebInterceptor(metadata.scope, method, bean);
                    concept.addInterceptor(interceptor);
                } else {
                    throw new CloudWebException("Return type must be boolean");
                }
            } else {
                WebInterceptor interceptor = new AnnotationWebInterceptor(metadata.scope, method, bean);
                concept.addInterceptor(interceptor);
            }
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.nonAnnotatedClasses.clear();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.concept = applicationContext.getBean(WebConcept.class);
    }

    @RequiredArgsConstructor
    private static class InterceptMetadata {

        final WebInterceptor.Scope scope;

        final Class<?> type;
    }

}
