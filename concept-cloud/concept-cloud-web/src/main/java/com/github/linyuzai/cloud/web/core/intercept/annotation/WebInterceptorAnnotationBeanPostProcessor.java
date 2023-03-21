package com.github.linyuzai.cloud.web.core.intercept.annotation;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
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
import java.util.stream.Collectors;

/**
 * 扫描标注了 {@link OnRequest}，{@link OnResponse} 的方法生成对应的拦截器并添加
 */
public class WebInterceptorAnnotationBeanPostProcessor implements BeanPostProcessor,
        ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    /**
     * 方法拦截器信息
     */
    private final List<InterceptMetadata> interceptMetadataList = Collections.synchronizedList(new ArrayList<>());

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
            Map<Method, Set<WebInterceptor.Scope>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<Set<WebInterceptor.Scope>>) method -> {
                        Set<WebInterceptor.Scope> set = new HashSet<>();
                        if (method.isAnnotationPresent(OnRequest.class)) {
                            set.add(WebInterceptor.Scope.REQUEST);
                        }
                        if (method.isAnnotationPresent(OnResponse.class)) {
                            set.add(WebInterceptor.Scope.RESPONSE);
                        }
                        return set;
                    });
            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(targetClass);
            } else {
                // Non-empty set of methods
                annotatedMethods.forEach((method, scopes) -> {
                    if (!scopes.isEmpty()) {
                        interceptMetadataList.add(new InterceptMetadata(method, bean, scopes));
                    }
                });
            }
        }
        return bean;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.nonAnnotatedClasses.clear();
        WebConcept webConcept = applicationContext.getBean(WebConcept.class);
        //获得所有方法拦截器工厂
        List<MethodWebInterceptorFactory> factories =
                applicationContext.getBeansOfType(MethodWebInterceptorFactory.class)
                        .values().stream()
                        .sorted(Comparator.comparingInt(MethodWebInterceptorFactory::getOrder))
                        .collect(Collectors.toList());
        for (InterceptMetadata metadata : interceptMetadataList) {
            //匹配工厂
            MethodWebInterceptorFactory factory = adaptFactory(metadata, factories);
            if (factory != null) {
                //创建拦截器并添加
                WebInterceptor interceptor = factory.create(metadata.scopes, metadata.method, metadata.bean);
                webConcept.addInterceptor(interceptor);
            }
        }
        interceptMetadataList.clear();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private MethodWebInterceptorFactory adaptFactory(InterceptMetadata metadata,
                                                     List<MethodWebInterceptorFactory> factories) {
        for (MethodWebInterceptorFactory factory : factories) {
            if (factory.support(metadata.scopes, metadata.method, metadata.bean)) {
                return factory;
            }
        }
        return null;
    }

    /**
     * 方法拦截器信息
     */
    @RequiredArgsConstructor
    private static class InterceptMetadata {

        /**
         * 方法
         */
        final Method method;

        /**
         * 实例
         */
        final Object bean;

        /**
         * 作用域
         */
        final Set<WebInterceptor.Scope> scopes;
    }

}
