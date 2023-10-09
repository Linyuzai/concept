package com.github.linyuzai.reactive.rxjava3.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import io.reactivex.rxjava3.core.Maybe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class RxJava3Object<T> implements ReactiveObject<T> {

    private final Maybe<T> maybe;

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
                if (object instanceof RxJava3Object) {
                    return ((RxJava3Object<? extends T>) object).maybe;
                }
                return Maybe.error(new IllegalArgumentException("Expect RxJava3Object but " + object.getClass()));
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
    }
}
