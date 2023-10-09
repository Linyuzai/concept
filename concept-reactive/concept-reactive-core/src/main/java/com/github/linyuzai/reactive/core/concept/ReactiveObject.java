package com.github.linyuzai.reactive.core.concept;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface ReactiveObject<T> {

    interface Factory {

        <T> ReactiveObject<T> empty();

        <T> ReactiveObject<T> just(T object);

        <T> ReactiveObject<T> defer(Supplier<? extends ReactiveObject<? extends T>> supplier);

        <T> ReactiveObject<T> error(Throwable e);

        <T> ReactiveObject<T> error(Supplier<Throwable> supplier);

        <T> ReactiveObject<T> fromCallable(Callable<? extends T> callable);

        <T> ReactiveObject<T> fromFuture(CompletableFuture<? extends T> future);

        <T> ReactiveObject<T> fromRunnable(Runnable runnable);

        <T> ReactiveObject<T> fromSupplier(Supplier<? extends T> supplier);
    }
}
