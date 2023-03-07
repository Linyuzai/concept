package com.github.linyuzai.concept.sample.feizhu.reconsitution.retry;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Retry<T> {

    T retry(Supplier<T> retry, Predicate<T> predicate, T defaultValue);
}
