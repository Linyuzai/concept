package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.result.WebResult;
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
        WebContext context = getOrCreateContext();
        context.put(HttpServletRequest.class, request);
        context.put(WebContext.Request.PATH, request.getRequestURI());
        webConcept.interceptRequest(context, ctx -> null, null);
    }

    @ExceptionHandler({Throwable.class})
    public void onError(Throwable e) {
        WebContext context = getOrCreateContext();
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
        WebContext context = getOrCreateContext();
        context.put(ServerHttpRequest.class, request);
        context.put(ServerHttpResponse.class, response);
        context.put(MethodParameter.class, returnType);
        context.put(MediaType.class, selectedContentType);
        context.put(HttpMessageConverter.class, selectedConverterType);
        context.put(WebContext.Response.BODY, body);
        Object result = webConcept.interceptResponse(context, ctx -> ctx.get(WebResult.class), body);
        invalidContext();
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
            if (handler instanceof HandlerMethod &&
                    (webConcept.isRequestInterceptionEnabled() ||
                            webConcept.isResponseInterceptionEnabled())) {
                getOrCreateContext().put(HandlerMethod.class, handler);
            }
            return true;
        }
    }
}
