package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.intercept.EmptyValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebResultValueReturner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
@RequiredArgsConstructor
@RestControllerAdvice
public class ServletCloudWebAdvice implements ResponseBodyAdvice<Object>, WebMvcConfigurer {

    private final WebContextFactory contextFactory;

    private final WebConcept webConcept;

    //@InitBinder
    public void initBinder(WebDataBinder binder) {

    }

    @ModelAttribute
    public void onRequest(HttpServletRequest request) {
        if (webConcept.isRequestInterceptionEnabled()) {
            WebContext context = getContext();
            WebInterceptor.Scope scope = context.get(WebInterceptor.Scope.class);
            if (scope == WebInterceptor.Scope.REQUEST) {
                //Async call: Mono
                return;
            }
            context.put(WebInterceptor.Scope.class, WebInterceptor.Scope.REQUEST);
            //context.put(HttpServletRequest.class, request);
            //context.put(WebContext.Request.PATH, request.getRequestURI());
            if (!context.containsKey(HandlerMethod.class)) {
                context.put(HandlerMethod.class, request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE));
            }
            webConcept.interceptRequest(context, EmptyValueReturner.INSTANCE);
        }
    }

    @ExceptionHandler({Throwable.class})
    public void onError(Throwable e) {
        WebContext context = getContext();
        context.put(Throwable.class, e);
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
        if (webConcept.isResponseInterceptionEnabled()) {
            WebContext context = getContext();
            context.put(WebInterceptor.Scope.class, WebInterceptor.Scope.RESPONSE);
            context.put(ServerHttpRequest.class, request);
            context.put(ServerHttpResponse.class, response);
            context.put(MethodParameter.class, returnType);
            context.put(MediaType.class, selectedContentType);
            context.put(HttpMessageConverter.class, selectedConverterType);
            context.put(WebContext.Response.BODY, body);
            result = webConcept.interceptResponse(context, WebResultValueReturner.INSTANCE);
        } else {
            result = body;
        }
        invalidContext();
        return result;
    }

    protected WebContext getContext() {
        return WebContextManager.get();
    }

    protected void validContext(WebContext context) {
        WebContextManager.set(context);
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
            WebContext context = contextFactory.create();
            context.put(HttpServletRequest.class, request);
            context.put(HttpServletResponse.class, response);
            context.put(HandlerMethod.class, handler);
            context.put(WebContext.Request.METHOD, request.getMethod());
            context.put(WebContext.Request.PATH, request.getRequestURI());
            validContext(context);
            return true;
        }
    }
}
