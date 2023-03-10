package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import reactor.core.publisher.Mono;

public class ReactiveWebResultValueReturner implements ValueReturner {

    public static final ReactiveWebResultValueReturner INSTANCE = new ReactiveWebResultValueReturner();

    @Override
    public Object value(WebContext context) {
        Object o = context.get(WebResult.class);
        //noinspection ReactiveStreamsUnusedPublisher
        return Mono.justOrEmpty(o);
    }
}
