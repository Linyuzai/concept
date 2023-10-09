package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import reactor.core.publisher.Flux;

public class ReactorCollection<T> implements ReactiveCollection {

    private Flux<T> flux;

    public static class FluxFactory implements Factory {

    }
}
