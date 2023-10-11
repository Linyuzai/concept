package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveCollection;
import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import com.github.linyuzai.reactive.core.concept.ReactivePublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class ReactorCollection<T> implements ReactiveCollection<T> {

    private final Flux<T> flux;

    @Override
    public <R> ReactiveCollection<R> flatMap(Function<? super T, ? extends ReactivePublisher<? extends R>> mapper) {
        return new ReactorCollection<>(flux.flatMap(t -> {
            ReactivePublisher<? extends R> apply = mapper.apply(t);
            return unwrap(apply);
        }));
    }

    @Override
    public ReactiveObject<List<T>> collectList() {
        return new ReactorObject<>(flux.collectList());
    }

    public static <T> Flux<? extends T> unwrap(ReactivePublisher<? extends T> publisher) {
        if (publisher instanceof ReactorCollection) {
            return ((ReactorCollection<? extends T>) publisher).flux;
        }
        return Flux.error(new IllegalArgumentException("Expect ReactorCollection but " + publisher.getClass()));
    }

    public static class FluxFactory implements Factory {

        @Override
        public <T> ReactiveCollection<T> empty() {
            return new ReactorCollection<>(Flux.empty());
        }

        @Override
        public <T> ReactiveCollection<T> fromIterable(Iterable<? extends T> it) {
            return new ReactorCollection<>(Flux.fromIterable(it));
        }
    }
}
