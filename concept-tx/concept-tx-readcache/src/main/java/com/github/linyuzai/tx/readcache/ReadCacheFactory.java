package com.github.linyuzai.tx.readcache;

import com.github.linyuzai.tx.core.concept.TransactionConcept;

public interface ReadCacheFactory {

    ReadCache create(TransactionConcept concept);
}
