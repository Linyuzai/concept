package com.github.linyuzai.tx.core.concept;

import com.github.linyuzai.tx.core.context.TransactionContext;
import com.github.linyuzai.tx.core.context.TransactionContextFactory;
import com.github.linyuzai.tx.core.engine.TransactionEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Supplier;

@Getter
@Setter
public abstract class AbstractTransactionConcept implements TransactionConcept {

    protected TransactionContextFactory contextFactory;

    protected TransactionEngine engine;

    @Override
    public <T> T tx(List<Runnable> list, Supplier<T> supplier) {
        Supplier<T> args = merge(list, supplier);
        return tx(args, () ->
                engine.execute(AbstractTransactionConcept.this, args));
    }

    protected boolean isInTransaction() {
        return currentTransaction() != null;
    }

    protected TransactionContext currentTransaction() {
        return TransactionContext.get();
    }

    protected TransactionContext beginTransaction() {
        TransactionContext context = contextFactory.create(this);
        TransactionContext.push(context);
        return context;
    }

    protected void endTransaction() {
        TransactionContext.poll();
    }

    protected abstract <T> T tx(Supplier<T> noTransaction, Supplier<T> inTransaction);

    private <T> Supplier<T> merge(List<Runnable> list, Supplier<T> supplier) {
        return () -> {
            if (list != null) {
                for (Runnable runnable : list) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
            if (supplier != null) {
                return supplier.get();
            }
            return null;
        };
    }
}
