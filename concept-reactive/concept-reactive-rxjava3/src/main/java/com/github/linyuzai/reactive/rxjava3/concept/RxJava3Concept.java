package com.github.linyuzai.reactive.rxjava3.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import com.github.linyuzai.reactive.core.concept.ReactiveConcept;
import com.github.linyuzai.reactive.core.concept.ReactiveObject;

public class RxJava3Concept implements ReactiveConcept {

    @Override
    public ReactiveObject.Factory objectFactory() {
        return new RxJava3Object.MaybeFactory();
    }

    @Override
    public ReactiveCollection.Factory collectionFactory() {
        return new RxJava3Collection.FlowableFactory();
    }
}
