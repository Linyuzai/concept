package com.github.linyuzai.reactive.core.concept;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ReactiveObject<T> extends ReactivePublisher<T> {

    <R> ReactiveObject<R> map(Function<? super T, ? extends R> mapper);

    <R> ReactiveObject<R> flatMap(Function<? super T, ? extends ReactivePublisher<? extends R>> transformer);

    ReactiveObject<T> doOnSuccess(Consumer<? super T> onSuccess);

    ReactiveObject<T> doOnError(Consumer<? super Throwable> onError);

    ReactiveObject<T> doAfterTerminate(Runnable runnable);

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

        <T1, T2, O> ReactiveObject<O> zip(ReactiveObject<? extends T1> ro1, ReactiveObject<?
                extends T2> ro2, BiFunction<? super T1, ? super T2, ? extends O> zipper);
    }
}
