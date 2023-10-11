package com.github.linyuzai.reactive.rxjava3.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import com.github.linyuzai.reactive.core.concept.ReactivePublisher;
import io.reactivex.rxjava3.core.Maybe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class RxJava3Object<T> implements ReactiveObject<T> {

    private final Maybe<T> maybe;

    @Override
    public <R> ReactiveObject<R> map(Function<? super T, ? extends R> mapper) {
        return new RxJava3Object<>(maybe.map(mapper::apply));
    }

    @Override
    public <R> ReactiveObject<R> flatMap(Function<? super T, ? extends ReactivePublisher<? extends R>> transformer) {
        return new RxJava3Object<>(maybe.flatMap(t -> {
            ReactivePublisher<? extends R> apply = transformer.apply(t);
            return unwrap(apply);
        }));
    }

    @Override
    public ReactiveObject<T> doOnSuccess(Consumer<? super T> onSuccess) {
        return new RxJava3Object<>(maybe.doOnSuccess(onSuccess::accept));
    }

    @Override
    public ReactiveObject<T> doOnError(Consumer<? super Throwable> onError) {
        return new RxJava3Object<>(maybe.doOnError(onError::accept));
    }

    @Override
    public ReactiveObject<T> doAfterTerminate(Runnable runnable) {
        return new RxJava3Object<>(maybe.doAfterTerminate(runnable::run));
    }

    public static <T> Maybe<? extends T> unwrap(ReactivePublisher<? extends T> publisher) {
        if (publisher instanceof RxJava3Object) {
            return ((RxJava3Object<? extends T>) publisher).maybe;
        }
        return Maybe.error(new IllegalArgumentException("Expect RxJava3Object but " + publisher.getClass()));
    }

    public static class MaybeFactory implements Factory {

        @Override
        public <T> ReactiveObject<T> empty() {
            return new RxJava3Object<>(Maybe.empty());
        }

        @Override
        public <T> ReactiveObject<T> just(T object) {
            return new RxJava3Object<>(Maybe.just(object));
        }

        @Override
        public <T> ReactiveObject<T> defer(Supplier<? extends ReactiveObject<? extends T>> supplier) {
            return new RxJava3Object<>(Maybe.defer(() -> {
                ReactiveObject<? extends T> object = supplier.get();
                return unwrap(object);
            }));
        }

        @Override
        public <T> ReactiveObject<T> error(Throwable e) {
            return new RxJava3Object<>(Maybe.error(e));
        }

        @Override
        public <T> ReactiveObject<T> error(Supplier<Throwable> supplier) {
            return new RxJava3Object<>(Maybe.error(supplier::get));
        }

        @Override
        public <T> ReactiveObject<T> fromCallable(Callable<? extends T> callable) {
            return new RxJava3Object<>(Maybe.fromCallable(callable));
        }

        @Override
        public <T> ReactiveObject<T> fromFuture(CompletableFuture<? extends T> future) {
            return new RxJava3Object<>(Maybe.fromFuture(future));
        }

        @Override
        public <T> ReactiveObject<T> fromRunnable(Runnable runnable) {
            return new RxJava3Object<>(Maybe.fromRunnable(runnable));
        }

        @Override
        public <T> ReactiveObject<T> fromSupplier(Supplier<? extends T> supplier) {
            return new RxJava3Object<>(Maybe.fromSupplier(supplier::get));
        }

        @Override
        public <T1, T2, O> ReactiveObject<O> zip(ReactiveObject<? extends T1> ro1, ReactiveObject<? extends T2> ro2, BiFunction<? super T1, ? super T2, ? extends O> zipper) {
            Maybe<? extends T1> maybe1 = unwrap(ro1);
            Maybe<? extends T2> maybe2 = unwrap(ro2);
            return new RxJava3Object<>(Maybe.zip(maybe1, maybe2, zipper::apply));
        }
    }
}
