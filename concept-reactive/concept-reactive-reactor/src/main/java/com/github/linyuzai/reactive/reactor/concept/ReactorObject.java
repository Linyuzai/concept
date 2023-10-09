package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import reactor.core.publisher.Mono;

public class ReactorObject<T> implements ReactiveObject {

    private Mono<T> mono;

    public static class MonoFactory implements Factory {

    }
}
