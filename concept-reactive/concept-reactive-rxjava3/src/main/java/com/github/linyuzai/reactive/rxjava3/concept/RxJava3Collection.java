package com.github.linyuzai.reactive.rxjava3.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import io.reactivex.rxjava3.core.Flowable;

public class RxJava3Collection<T> implements ReactiveCollection {

    private Flowable<T> flowable;

    public static class FlowableFactory implements Factory {

    }
}
