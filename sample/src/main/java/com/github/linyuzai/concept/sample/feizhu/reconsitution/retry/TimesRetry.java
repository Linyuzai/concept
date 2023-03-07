package com.github.linyuzai.concept.sample.feizhu.reconsitution.retry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class TimesRetry<T> implements Retry<T> {

    private final int times;

    @Override
    public T retry(Supplier<T> retry, Predicate<T> predicate, T defaultValue) {
        for (int i = 0; i < times; i++) {
            T t = retry.get();
            if (predicate.test(t)) {
                return t;
            }
        }
        return defaultValue;
    }
}
