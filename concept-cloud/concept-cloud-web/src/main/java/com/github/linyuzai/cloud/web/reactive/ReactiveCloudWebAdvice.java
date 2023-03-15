package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import lombok.Getter;
import org.reactivestreams.Publisher;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("all")
@Getter
@RestControllerAdvice
public class ReactiveCloudWebAdvice extends ResponseBodyResultHandler {

    private static boolean hasMethod;

    static {
        try {
            hasMethod = Mono.class.getMethod("deferContextual", Function.class) != null;
        } catch (Throwable ignore) {
        }
    }

    private final WebConcept webConcept;

    public ReactiveCloudWebAdvice(List<HttpMessageWriter<?>> writers,
                                  RequestedContentTypeResolver resolver,
                                  ReactiveAdapterRegistry registry,
                                  WebConcept webConcept) {
        super(writers, resolver, registry);
        this.webConcept = webConcept;
        setOrder(getOrder() - 1);
    }

    @ModelAttribute
    public Mono<Void> onRequest(ServerWebExchange exchange) {
        if (webConcept.isRequestInterceptionEnabled()) {
            return getContext().doOnNext(context -> {
                setHandlerMethod(context, exchange);
                context.put(WebInterceptor.Scope.class, WebInterceptor.Scope.REQUEST);
            }).flatMap(it -> {
                return (Mono<Void>) webConcept.interceptRequest(it, ReactiveEmptyValueReturner.INSTANCE);
            });
        } else {
            return Mono.empty();
        }
    }

    @ExceptionHandler({Throwable.class})
    public Mono<Void> onError(Throwable e) {
        return getContext().doOnNext(context -> {
            context.put(Throwable.class, e);
        }).then();
    }

    @NonNull
    @Override
    public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, @NonNull HandlerResult result) {
        if (webConcept.isResponseInterceptionEnabled()) {
            Object returnValue = result.getReturnValue();
            Mono<Object> mono;
            if (returnValue instanceof Publisher) {
                mono = Mono.from((Publisher<?>) returnValue);
            } else {
                mono = Mono.justOrEmpty(returnValue);
            }
            return mono.switchIfEmpty(getContext()).flatMap(valueOrContext -> {
                if (valueOrContext instanceof WebContext) {
                    return Mono.just((WebContext) valueOrContext);
                } else {
                    return getContext().doOnNext(context -> {
                        context.put(WebContext.Response.BODY, valueOrContext);
                    });
                }
            }).doOnNext(context -> {
                setHandlerMethod(context, exchange);
                context.put(WebInterceptor.Scope.class, WebInterceptor.Scope.RESPONSE);
                context.put(HandlerResult.class, result);
                context.put(MethodParameter.class, result.getReturnTypeSource());
                //context.put(ServerWebExchange.class, exchange);
            }).flatMap(context -> {
                return (Mono<?>) webConcept.interceptResponse(context, ReactiveWebResultValueReturner.INSTANCE);
            }).flatMap(webResult -> {
                MethodParameter parameter = new WebResultMethodParameter(result.getReturnTypeSource(), webResult);
                return writeBody(webResult, parameter, exchange);
            });
        } else {
            return super.handleResult(exchange, result);
        }
    }

    protected Mono<WebContext> getContext() {
        if (hasMethod) {
            return Mono.deferContextual(contextView -> Mono.just(contextView.get(WebContext.class)));
        } else {
            return Mono.subscriberContext().map(ctx -> ctx.get(WebContext.class));
        }
    }

    protected void setHandlerMethod(WebContext context, ServerWebExchange exchange) {
        if (context.get(HandlerMethod.class) == null) {
            context.put(HandlerMethod.class, exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE));
        }
    }

    public static class WebResultMethodParameter extends MethodParameter {

        private final Class<?> type;

        public WebResultMethodParameter(MethodParameter original, Object webResult) {
            super(original);
            if (webResult == null) {
                type = void.class;
            } else {
                type = webResult.getClass();
            }
        }

        @Override
        public Class<?> getParameterType() {
            return type;
        }

        @Override
        public Type getGenericParameterType() {
            return type;
        }
    }
}
