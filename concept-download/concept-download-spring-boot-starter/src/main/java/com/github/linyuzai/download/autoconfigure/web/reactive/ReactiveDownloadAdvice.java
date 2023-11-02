package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.autoconfigure.properties.DownloadConceptAdvice;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Getter
public class ReactiveDownloadAdvice extends ResponseBodyResultHandler {

    private static boolean hasMethod;

    static {
        try {
            hasMethod = Mono.class.getMethod("deferContextual", Function.class) != null;
        } catch (Throwable ignore) {
        }
    }

    private final DownloadConcept concept;

    public ReactiveDownloadAdvice(List<HttpMessageWriter<?>> writers,
                                  RequestedContentTypeResolver resolver,
                                  ReactiveAdapterRegistry registry,
                                  DownloadConcept concept) {
        super(writers, resolver, registry);
        this.concept = concept;
        //setOrder(getOrder() - 1);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, @NonNull HandlerResult result) {
        HandlerMethod handlerMethod = getHandlerMethod(exchange);
        if (handlerMethod != null && handlerMethod.hasMethodAnnotation(Download.class)) {
            MethodParameter returnType = handlerMethod.getReturnType();
            Object returnValue = result.getReturnValue();
            DownloadOptions options = DownloadConceptAdvice.buildOptions(returnType, returnValue, null);
            return (Mono<Void>) concept.download(options);
        } else {
            return super.handleResult(exchange, result);
        }
    }

    protected HandlerMethod getHandlerMethod(ServerWebExchange exchange) {
        Object attribute = exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (attribute instanceof HandlerMethod) {
            return (HandlerMethod) attribute;
        }
        return null;
    }
}
