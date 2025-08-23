package com.github.linyuzai.tx.core.context;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class TransactionContext {

    private static final ThreadLocal<Deque<TransactionContext>> TX_CTX = new InheritableThreadLocal<>() {

        @Override
        protected Deque<TransactionContext> initialValue() {
            return new ArrayDeque<>();
        }
    };

    public static TransactionContext get() {
        TransactionContext context = TX_CTX.get().peek();
        if (context == null) {
            TX_CTX.remove();
        }
        return context;
    }

    public static void push(TransactionContext context) {
        TX_CTX.get().push(context);
    }

    public static TransactionContext poll() {
        Deque<TransactionContext> deque = TX_CTX.get();
        TransactionContext context = deque.poll();
        if (deque.isEmpty()) {
            TX_CTX.remove();
        }
        return context;
    }

    public abstract void set(Object key, Object value);

    public abstract <T> T get(Object key);
}
