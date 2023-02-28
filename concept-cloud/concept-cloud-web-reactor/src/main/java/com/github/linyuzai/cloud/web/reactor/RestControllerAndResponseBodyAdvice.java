/*
package com.github.linyuzai.cloud.web.reactor;

import com.github.linyuzai.chain.core.Return;
import com.github.linyuzai.chain.reactor.ReactorReturn;
import com.github.linyuzai.cloud.web.CloudWebProperties;
import com.github.linyuzai.cloud.web.context.WebContext;
import com.github.linyuzai.cloud.web.context.WebContextFactory;
import com.github.linyuzai.cloud.web.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.intercept.WebInterceptorChainFactory;
import com.github.linyuzai.cloud.web.result.WebResult;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestControllerAndResponseBodyAdvice implements BeanPostProcessor {

    private final CloudWebProperties properties;

    private final WebContextFactory contextFactory;

    private final WebInterceptorChainFactory chainFactory;

    private final List<WebInterceptor> requestInterceptors;

    private final List<WebInterceptor> responseInterceptors;

    private final List<WebInterceptor> exceptionInterceptors;

    public RestControllerAndResponseBodyAdvice(CloudWebProperties properties,
                                               WebContextFactory contextFactory,
                                               WebInterceptorChainFactory chainFactory,
                                               List<WebInterceptor> interceptors) {
        this.properties = properties;
        this.contextFactory = contextFactory;
        this.chainFactory = chainFactory;
        this.requestInterceptors = interceptors.stream()
                .filter(it -> it.getScopes().contains(WebInterceptor.Scope.REQUEST))
                .collect(Collectors.toList());
        this.responseInterceptors = interceptors.stream()
                .filter(it -> it.getScopes().contains(WebInterceptor.Scope.RESPONSE))
                .collect(Collectors.toList());
        this.exceptionInterceptors = interceptors.stream()
                .filter(it -> it.getScopes().contains(WebInterceptor.Scope.EXCEPTION))
                .collect(Collectors.toList());
    }

    //@InitBinder
    public void initBinder(WebDataBinder binder) {

    }

    @ModelAttribute
    public void modelAttribute(HttpServletRequest request) {
        if (properties.getIntercept().getRequest().isEnabled()) {
            WebContext context = getOrCreateContext();
            context .put(HttpServletRequest.class, request);
            chainFactory.create(requestInterceptors).next(context);
        }
    }

    @ExceptionHandler({Throwable.class})
    public Object handleException(Throwable e) {
        if (properties.getIntercept().getError().isEnabled()) {
            WebContext context = getOrCreateContext()
                    .put(Throwable.class, e);
            chainFactory.create(exceptionInterceptors).next(context);
            return context.get(WebResult.class, e);
        } else {
            return e;
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    public class ResponseBodyAdviceHandlerResultHandler implements HandlerResultHandler {

        private HandlerResultHandler handler;

        @Override
        public boolean supports(@NonNull HandlerResult result) {
            return handler.supports(result);
        }

        @NonNull
        @Override
        public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, @NonNull HandlerResult result) {
            //advice
            return handler.handleResult(exchange, beforeBodyWrite(exchange, result));
        }
    }

    public HandlerResult beforeBodyWrite(@NonNull ServerWebExchange exchange, @NonNull HandlerResult result) {
        */
/*if (properties.getIntercept().getResponse().isEnabled()) {
            getOrCreateContext().map(it -> {
                WebContext context = it.put(ServerWebExchange.class, exchange)
                        .put(HandlerResult.class, result);
                return chainFactory.create(responseInterceptors).next(context);
            }).map(it -> {

            })
            return context.get(WebResult.class);
        } else {
            return result;
        }*//*

        return result;
        //TODO reactive 会有问题
        //WebContext.getGlobal().reset();
    }

    protected Return<WebContext> getOrCreateContext() {
        Return<WebContext> rwc = new ReactorReturn<>();
        return rwc.set(Mono.subscriberContext().map(it -> {
            if (it.hasKey(WebContext.class)) {
                return it.get(WebContext.class);
            } else {
                WebContext newContext = contextFactory.create();
                it.put(WebContext.class, newContext);
                return newContext;
            }
        }));
    }

    protected WebContext reset() {
        Mono.subscriberContext().map(it -> {
            it.delete(WebContext.class);
            return it;
        });
    }

    public class HandlerMethodWebFilter implements WebFilter {

        RequestMappingHandlerMapping mapping;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            mapping.getHandler(exchange);
            */
/*if (handler instanceof HandlerMethod && (
                    properties.getIntercept().getRequest().isEnabled() ||
                            properties.getIntercept().getResponse().isEnabled() ||
                            properties.getIntercept().getError().isEnabled())) {
                getOrCreateContext().put(HandlerMethod.class, handler);
            }*//*

            return null;
        }
    }
}
*/
