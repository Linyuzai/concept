package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebError;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebResponse;
import lombok.SneakyThrows;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class WebInterceptorAnnotationBeanPostProcessor implements BeanPostProcessor, SmartInitializingSingleton {

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
        if (AnnotationUtils.isCandidateClass(targetClass,
                Arrays.asList(OnWebRequest.class, OnWebResponse.class, OnWebError.class))) {
            Map<Method, Set<WebInterceptor.Scope>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<Set<WebInterceptor.Scope>>) method -> {
                        Set<WebInterceptor.Scope> scopes = new HashSet<>();
                        if (method.isAnnotationPresent(OnWebRequest.class)) {
                            scopes.add(WebInterceptor.Scope.REQUEST);
                        }
                        if (method.isAnnotationPresent(OnWebResponse.class)) {
                            scopes.add(WebInterceptor.Scope.RESPONSE);
                        }
                        if (method.isAnnotationPresent(OnWebError.class)) {
                            scopes.add(WebInterceptor.Scope.ERROR);
                        }
                        return scopes;
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

    public void processWebInterceptor(Set<WebInterceptor.Scope> scopes, Method method, Object bean) {
        method.setAccessible(true);
        if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) {
            new PredicateWebInterceptor(new Predicate<WebContext>() {

                @SneakyThrows
                @Override
                public boolean test(WebContext context) {
                    return (boolean) method.invoke(bean);
                }
            });
        } else {

        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.nonAnnotatedClasses.clear();
    }
}
