package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import com.github.linyuzai.reactive.core.concept.ReactivePublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class ReactorObject<T> implements ReactiveObject<T> {

    private final Mono<T> mono;

    @Override
    public <R> ReactiveObject<R> map(Function<? super T, ? extends R> mapper) {
        return new ReactorObject<>(mono.map(mapper));
    }

    @Override
    public <R> ReactiveObject<R> flatMap(Function<? super T, ? extends ReactivePublisher<? extends R>> transformer) {
        return new ReactorObject<>(mono.flatMap(t -> {
            ReactivePublisher<? extends R> apply = transformer.apply(t);
            return unwrap(apply);
        }));
    }

    @Override
    public ReactiveObject<T> doOnSuccess(Consumer<? super T> onSuccess) {
        return new ReactorObject<>(mono.doOnSuccess(onSuccess));
    }

    @Override
    public ReactiveObject<T> doOnError(Consumer<? super Throwable> onError) {
        return new ReactorObject<>(mono.doOnError(onError));
    }

    @Override
    public ReactiveObject<T> doAfterTerminate(Runnable runnable) {
        return new ReactorObject<>(mono.doAfterTerminate(runnable));
    }

    public static <T> Mono<? extends T> unwrap(ReactivePublisher<? extends T> publisher) {
        if (publisher instanceof ReactorObject) {
            return ((ReactorObject<? extends T>) publisher).mono;
        }
        return Mono.error(new IllegalArgumentException("Expect ReactorObject but " + publisher.getClass()));
    }

    public static class MonoFactory implements Factory {

        @Override
        public <T> ReactiveObject<T> empty() {
            return new ReactorObject<>(Mono.empty());
        }

        @Override
        public <T> ReactiveObject<T> just(T object) {
            return new ReactorObject<>(Mono.just(object));
        }

        @Override
        public <T> ReactiveObject<T> defer(Supplier<? extends ReactiveObject<? extends T>> supplier) {
            return new ReactorObject<>(Mono.defer(() -> {
                ReactiveObject<? extends T> object = supplier.get();
                return unwrap(object);
            }));
        }

        @Override
        public <T> ReactiveObject<T> error(Throwable e) {
            return new ReactorObject<>(Mono.error(e));
        }

        @Override
        public <T> ReactiveObject<T> error(Supplier<Throwable> supplier) {
            return new ReactorObject<>(Mono.error(supplier));
        }

        @Override
        public <T> ReactiveObject<T> fromCallable(Callable<? extends T> callable) {
            return new ReactorObject<>(Mono.fromCallable(callable));
        }

        @Override
        public <T> ReactiveObject<T> fromFuture(CompletableFuture<? extends T> future) {
            return new ReactorObject<>(Mono.fromFuture(future));
        }

        @Override
        public <T> ReactiveObject<T> fromRunnable(Runnable runnable) {
            return new ReactorObject<>(Mono.fromRunnable(runnable));
        }

        @Override
        public <T> ReactiveObject<T> fromSupplier(Supplier<? extends T> supplier) {
            return new ReactorObject<>(Mono.fromSupplier(supplier));
        }

        @Override
        public <T1, T2, O> ReactiveObject<O> zip(ReactiveObject<? extends T1> ro1, ReactiveObject<? extends T2> ro2, BiFunction<? super T1, ? super T2, ? extends O> zipper) {
            Mono<? extends T1> mono1 = unwrap(ro1);
            Mono<? extends T2> mono2 = unwrap(ro2);
            return new ReactorObject<>(Mono.zip(mono1, mono2, zipper));
        }
    }
}
