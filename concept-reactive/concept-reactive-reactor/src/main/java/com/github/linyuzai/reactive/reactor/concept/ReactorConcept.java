package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import com.github.linyuzai.reactive.core.concept.ReactiveConcept;
import com.github.linyuzai.reactive.core.concept.ReactiveObject;

public class ReactorConcept implements ReactiveConcept {

    private final ReactiveObject.Factory objectFactory = new ReactorObject.MonoFactory();

    private final ReactiveCollection.Factory collectionFactory = new ReactorCollection.FluxFactory();

    @Override
    public ReactiveObject.Factory objectFactory() {
        return objectFactory;
    }

    @Override
    public ReactiveCollection.Factory collectionFactory() {
        return collectionFactory;
    }
}
