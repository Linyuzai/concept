package com.github.linyuzai.download.web.reactive;

import com.github.linyuzai.download.core.concept.ValueContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class MonoValue extends Mono<Void> implements ValueContainer {

    @Setter
    @Getter
    private Object value;

    @Override
    public void subscribe(@NonNull CoreSubscriber<? super Void> actual) {
        //can not be called
    }
}
