package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.Getter;
import org.reactivestreams.Publisher;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings("all")
@Getter
@RestControllerAdvice
public class ReactiveCloudWebAdvice extends ResponseBodyResultHandler {

    private final WebContextFactory contextFactory;

    private final WebConcept webConcept;

    public ReactiveCloudWebAdvice(List<HttpMessageWriter<?>> writers,
                                  RequestedContentTypeResolver resolver,
                                  ReactiveAdapterRegistry registry,
                                  WebContextFactory contextFactory,
                                  WebConcept webConcept) {
        super(writers, resolver, registry);
        this.contextFactory = contextFactory;
        this.webConcept = webConcept;
        setOrder(getOrder() - 1);
    }

    @ModelAttribute
    public Mono<Void> onRequest(ServerWebExchange exchange) {
        WebContext context = getOrCreateContext();
        context.put(ServerWebExchange.class, exchange);
        context.put(ServerHttpRequest.class, exchange.getRequest());
        context.put(ServerHttpResponse.class, exchange.getResponse());
        context.put(WebContext.Request.PATH, exchange.getRequest().getPath().value());
        return (Mono<Void>) webConcept.interceptRequest(context, ctx -> Mono.empty(), Mono.empty());
    }

    @ExceptionHandler({Throwable.class})
    public Mono<Void> onError(Throwable e) {
        WebContext context = getOrCreateContext();
        context.put(Throwable.class, e);
        /*return webConcept.interceptError(context, ctx -> {
            Throwable t = ctx.get(Throwable.class);
            return t == null ? e : t;
        }, e);*/
        return (Mono<Void>) webConcept.interceptError(context, ctx -> {
            Throwable t = ctx.get(Throwable.class);
            if (t == null) {
                return Mono.just(e);
            } else {
                return Mono.just(t);
            }
        }, Mono.just(e));
    }

    @NonNull
    @Override
    public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, @NonNull HandlerResult result) {
        Object returnValue = result.getReturnValue();
        Mono<Object> mono;
        if (returnValue instanceof Publisher) {
            mono = Mono.from((Publisher<?>) returnValue);
        } else {
            mono = Mono.justOrEmpty(returnValue);
        }
        return mono.flatMap(it -> {
            WebContext context = getOrCreateContext();
            //context.put(ServerWebExchange.class, exchange);
            context.put(HandlerResult.class, result);
            context.put(MethodParameter.class, result.getReturnTypeSource());
            context.put(WebContext.Response.BODY, it);
            return (Mono<WebResult<?>>) webConcept.interceptResponse(context,
                    ctx -> Mono.justOrEmpty((Object) ctx.get(WebResult.class)),
                    Mono.justOrEmpty(it));
        }).flatMap(it -> {
            MethodParameter parameter = result.getReturnTypeSource();
            MethodParameter newParameter = new MethodParameter(parameter) {

                @Override
                public Class<?> getParameterType() {
                    return WebResult.class;
                }

                @Override
                public Type getGenericParameterType() {
                    return WebResult.class;
                }
            };
            /*HandlerResult newHandlerResult = new HandlerResult(result.getHandler(),
                    it, newParameter, result.getBindingContext());
            return super.handleResult(exchange, newHandlerResult);*/
            return writeBody(it, newParameter, exchange);
        }).doAfterTerminate(new Runnable() {
            @Override
            public void run() {
                System.out.println("invalidContext");
                //invalidContext();
            }
        });
    }

    protected WebContext getOrCreateContext() {
        return contextFactory.create();
    }
}
