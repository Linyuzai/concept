package com.github.linyuzai.tx.readcache;

import com.github.linyuzai.tx.core.concept.AbstractTransactionConcept;
import com.github.linyuzai.tx.core.context.TransactionContext;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
public class ReadCacheTransactionConcept extends AbstractTransactionConcept {

    private ReadCacheFactory readCacheFactory;

    @Override
    protected <T> T tx(Supplier<T> noTransaction, Supplier<T> inTransaction) {
        TransactionContext current = currentTransaction();
        TransactionContext context = beginTransaction();
        try {
            if (current == null) {
                ReadCache readCache = readCacheFactory.create(this);
                context.set(ReadCache.class, readCache);
                readCache.setMode(ReadCache.Mode.WRITE);
                noTransaction.get();
                readCache.setMode(ReadCache.Mode.READ);
                return inTransaction.get();
            } else {
                ReadCache readCache = current.get(ReadCache.class);
                if (readCache == null) {
                    throw new IllegalStateException("Read cache is null");
                }
                context.set(ReadCache.class, readCache);
                if (readCache.getMode() == ReadCache.Mode.READ) {
                    return inTransaction.get();
                } else {
                    return noTransaction.get();
                }
            }
        } finally {
            endTransaction();
        }
    }
}
