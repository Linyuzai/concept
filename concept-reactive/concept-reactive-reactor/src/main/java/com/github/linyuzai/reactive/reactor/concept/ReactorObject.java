package com.github.linyuzai.reactive.reactor.concept;

import com.github.linyuzai.reactive.core.concept.ReactiveObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public class ReactorObject<T> implements ReactiveObject<T> {

    private Mono<T> mono;

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
                if (object instanceof ReactorObject) {
                    return ((ReactorObject<? extends T>) object).mono;
                }
                return Mono.error(new IllegalArgumentException("Expect ReactorObject but " + object.getClass()));
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
    }
}
