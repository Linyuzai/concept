package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import com.github.linyuzai.reactive.core.concept.ReactiveConcept;
import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import reactor.core.publisher.Mono;

public class ReactorConcept implements ReactiveConcept {

    @Override
    public ReactiveObject.Factory objectFactory() {
        return new ReactorObject.MonoFactory();
    }

    @Override
    public ReactiveCollection.Factory collectionFactory() {
        return null;
    }
}
