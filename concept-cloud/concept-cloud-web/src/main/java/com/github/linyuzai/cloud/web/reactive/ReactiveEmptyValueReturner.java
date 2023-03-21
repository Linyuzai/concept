package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import reactor.core.publisher.Mono;

/**
 * webflux 返回 {@link Mono#empty()}
 */
public class ReactiveEmptyValueReturner implements ValueReturner {

    public static final ReactiveEmptyValueReturner INSTANCE = new ReactiveEmptyValueReturner();

    @Override
    public Object value(WebContext context) {
        //noinspection ReactiveStreamsUnusedPublisher
        return Mono.empty();
    }
}
