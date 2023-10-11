package com.github.linyuzai.reactive.rxjava3.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import com.github.linyuzai.reactive.core.concept.ReactivePublisher;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class RxJava3Collection<T> implements ReactiveCollection<T> {

    private final Flowable<T> flowable;

    @Override
    public <R> ReactiveCollection<R> flatMap(Function<? super T, ? extends ReactivePublisher<? extends R>> mapper) {
        return new RxJava3Collection<>(flowable.flatMap(t -> {
            ReactivePublisher<? extends R> apply = mapper.apply(t);
            return unwrap(apply);
        }));
    }

    @Override
    public ReactiveObject<List<T>> collectList() {
        return new RxJava3Object<>(Maybe.fromSingle(flowable.collect(Collectors.toList())));
    }

    public static <T> Flowable<? extends T> unwrap(ReactivePublisher<? extends T> publisher) {
        if (publisher instanceof RxJava3Collection) {
            return ((RxJava3Collection<? extends T>) publisher).flowable;
        }
        return Flowable.error(new IllegalArgumentException("Expect RxJava3Collection but " + publisher.getClass()));
    }

    public static class FlowableFactory implements Factory {

        @Override
        public <T> ReactiveCollection<T> empty() {
            return new RxJava3Collection<>(Flowable.empty());
        }

        @Override
        public <T> ReactiveCollection<T> fromIterable(Iterable<? extends T> it) {
            return new RxJava3Collection<>(Flowable.fromIterable(it));
        }
    }
}
