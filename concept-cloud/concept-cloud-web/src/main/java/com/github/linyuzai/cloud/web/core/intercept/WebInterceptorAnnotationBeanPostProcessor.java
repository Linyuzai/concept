package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.CloudWebException;
import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class WebInterceptorAnnotationBeanPostProcessor implements BeanPostProcessor,
        ApplicationContextAware, SmartInitializingSingleton, SmartLifecycle {

    private ApplicationContext applicationContext;

    private final List<WebInterceptor> webInterceptors = Collections.synchronizedList(new ArrayList<>());

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
                Arrays.asList(OnRequest.class, OnResponse.class))) {
            Map<Method, List<InterceptMetadata>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<List<InterceptMetadata>>) method -> {
                        List<InterceptMetadata> list = new ArrayList<>();
                        OnRequest onRequest = method.getAnnotation(OnRequest.class);
                        if (onRequest != null) {
                            list.add(new InterceptMetadata(WebInterceptor.Scope.REQUEST, onRequest.value()));
                        }
                        OnResponse onResponse = method.getAnnotation(OnResponse.class);
                        if (onResponse != null) {
                            list.add(new InterceptMetadata(WebInterceptor.Scope.RESPONSE, onResponse.value()));
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
                    WebInterceptor interceptor =
                            new AnnotationPredicateWebInterceptor(metadata.scope, method, bean);
                    webInterceptors.add(interceptor);
                } else {
                    throw new CloudWebException("Return type must be boolean");
                }
            } else if (metadata.type == WebResult.class) {
                if (WebResult.class.isAssignableFrom(method.getReturnType())) {
                    WebInterceptor interceptor =
                            new AnnotationRewriteWebResultWebInterceptor(metadata.scope, method, bean);
                    webInterceptors.add(interceptor);
                } else {
                    throw new CloudWebException("Return type must be WebResult");
                }
            } else {
                WebInterceptor interceptor = new AnnotationWebInterceptor(metadata.scope, method, bean);
                webInterceptors.add(interceptor);
            }
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.nonAnnotatedClasses.clear();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {
        WebConcept webConcept = applicationContext.getBean(WebConcept.class);
        webInterceptors.forEach(webConcept::addInterceptor);
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @RequiredArgsConstructor
    private static class InterceptMetadata {

        final WebInterceptor.Scope scope;

        final Class<?> type;
    }

}
