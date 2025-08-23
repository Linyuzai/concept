package com.github.linyuzai.tx.core.concept;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public interface TransactionConcept {

    default void tx(Runnable... arr) {
        tx(Arrays.asList(arr), null);
    }

    default <T> T tx(Supplier<T> supplier) {
        return tx(Collections.emptyList(), supplier);
    }

    default <T> T tx(Runnable runnable, Supplier<T> supplier) {
        return tx(Collections.singletonList(runnable), supplier);
    }

    default <T> T tx(Runnable runnable1, Runnable runnable2, Supplier<T> supplier) {
        return tx(Arrays.asList(runnable1, runnable2), supplier);
    }

    default <T> T tx(Runnable runnable1, Runnable runnable2, Runnable runnable3, Supplier<T> supplier) {
        return tx(Arrays.asList(runnable1, runnable2, runnable3), supplier);
    }

    <T> T tx(List<Runnable> list, Supplier<T> supplier);
}
