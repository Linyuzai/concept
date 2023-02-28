package com.github.linyuzai.chain.reactor;

import com.github.linyuzai.chain.core.Return;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@SuppressWarnings("unchecked")
public class ReactorReturn<T> implements Return<T> {

    private Publisher<T> value;

    @Override
    public Return<T> empty() {
        this.value = Mono.empty();
        return this;
    }

    @Override
    public Return<T> set(Object value) {
        this.value = (Publisher<T>) value;
        return this;
    }

    @Override
    public Return<T> wrap(T value) {
        if (value instanceof Publisher) {
            this.value = Mono.from((Publisher<? extends T>) value);
        } else {
            this.value = Mono.justOrEmpty(value);
        }
        return this;
    }

    @Override
    public <R> R unwrap() {
        return (R) value;
    }
}
