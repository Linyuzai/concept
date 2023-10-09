package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Getter
@RequiredArgsConstructor
public class ReactorCollection<T> implements ReactiveCollection<T> {

    private final Flux<T> flux;

    public static class FluxFactory implements Factory {

        @Override
        public <T> ReactiveCollection<T> empty() {
            return new ReactorCollection<>(Flux.empty());
        }
    }
}
