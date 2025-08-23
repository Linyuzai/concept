package com.github.linyuzai.tx.core.engine;

import com.github.linyuzai.tx.core.concept.TransactionConcept;

import java.util.function.Supplier;

public interface TransactionEngine {

    <T> T execute(TransactionConcept concept, Supplier<T> supplier);
}
