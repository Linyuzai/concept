package com.github.linyuzai.reactive.rxjava3.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import io.reactivex.rxjava3.core.Maybe;

public class RxJava3Object<T> implements ReactiveObject {

    private Maybe<T> maybe;

    public static class MaybeFactory implements Factory {

    }
}
