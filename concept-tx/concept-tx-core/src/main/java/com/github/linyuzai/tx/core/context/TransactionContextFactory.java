package com.github.linyuzai.tx.core.context;

import com.github.linyuzai.tx.core.concept.TransactionConcept;

public interface TransactionContextFactory {

    TransactionContext create(TransactionConcept concept);
}
