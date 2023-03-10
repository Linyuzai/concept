package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReactiveWebInterceptorChain implements WebInterceptorChain {

    private final int index;

    private final List<WebInterceptor> interceptors;

    private final WebInterceptorChainFactory chainFactory;

    @Override
    public Object next(WebContext context, ValueReturner returner) {
        //noinspection ReactiveStreamsUnusedPublisher
        return Mono.defer(() -> {
            if (this.index < interceptors.size()) {
                WebInterceptor interceptor = interceptors.get(this.index);
                WebInterceptorChain chain = chainFactory.create(this.index + 1, interceptors);
                return (Mono<?>) interceptor.intercept(context, returner, chain);
            } else {
                return (Mono<?>) returner.value(context);
            }
        });
    }
}
