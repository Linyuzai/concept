package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.CloudWebProperties;
import com.github.linyuzai.cloud.web.context.WebContext;
import com.github.linyuzai.cloud.web.context.WebContextFactory;
import com.github.linyuzai.cloud.web.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.intercept.WebInterceptorChainFactory;
import com.github.linyuzai.cloud.web.result.WebResult;
import lombok.Getter;
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
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RestControllerAdvice
public class RestControllerAndResponseBodyAdvice implements ResponseBodyAdvice<Object>, WebMvcConfigurer {

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
            context.put(HttpServletRequest.class, request);
            chainFactory.create(requestInterceptors).next(context);
        }
    }

    @ExceptionHandler({Throwable.class})
    public Object handleException(Throwable e) {
        if (properties.getIntercept().getError().isEnabled()) {
            WebContext context = getOrCreateContext();
            context.put(Throwable.class, e);
            chainFactory.create(exceptionInterceptors).next(context);
            return context.get(WebResult.class, e);
        } else {
            return e;
        }
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        Object result;
        if (properties.getIntercept().getResponse().isEnabled()) {
            WebContext context = getOrCreateContext();
            context.put(ServerHttpRequest.class, request);
            context.put(ServerHttpResponse.class, response);
            context.put(MethodParameter.class, returnType);
            context.put(MediaType.class, selectedContentType);
            context.put(HttpMessageConverter.class, selectedConverterType);
            context.put(WebContext.Key.RESPONSE_BODY, body);
            chainFactory.create(responseInterceptors).next(context);
            result = context.get(WebResult.class);
        } else {
            result = body;
        }
        invalidContext();
        /*RequestMappingHandlerMapping mapping;
        mapping.getHandler(request)*/
        return result;
    }

    protected WebContext getOrCreateContext() {
        WebContext context = WebContextManager.get();
        if (context == null) {
            WebContext create = contextFactory.create();
            WebContextManager.set(create);
            return create;
        } else {
            return context;
        }
    }

    protected void invalidContext() {
        WebContextManager.invalid();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerMethodHandlerInterceptor());
    }

    public class HandlerMethodHandlerInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull Object handler) throws Exception {
            if (handler instanceof HandlerMethod && (
                    properties.getIntercept().getRequest().isEnabled() ||
                            properties.getIntercept().getResponse().isEnabled() ||
                            properties.getIntercept().getError().isEnabled())) {
                getOrCreateContext().put(HandlerMethod.class, handler);
            }
            return true;
        }
    }
}
