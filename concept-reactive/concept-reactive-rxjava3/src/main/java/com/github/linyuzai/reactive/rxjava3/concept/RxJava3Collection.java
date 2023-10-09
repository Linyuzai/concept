package com.github.linyuzai.reactive.rxjava3.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import io.reactivex.rxjava3.core.Flowable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RxJava3Collection<T> implements ReactiveCollection<T> {

    private final Flowable<T> flowable;

    public static class FlowableFactory implements Factory {

        @Override
        public <T> ReactiveCollection<T> empty() {
            return new RxJava3Collection<>(Flowable.empty());
        }
    }
}
