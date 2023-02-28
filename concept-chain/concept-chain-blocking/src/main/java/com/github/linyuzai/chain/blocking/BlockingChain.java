package com.github.linyuzai.chain.blocking;

import com.github.linyuzai.chain.core.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class BlockingChain<T, CTX extends Context, CH extends Chain<T, CTX>> implements Chain<T, CTX> {

    private int index;

    private final List<? extends Invoker<T, CTX, CH>> invokers;

    private final Function<CTX, Return<T>> function;

    @SuppressWarnings("unchecked")
    @Override
    public Return<T> next(CTX context) {
        if (index < invokers.size()) {
            Invoker<T, CTX, CH> invoker = invokers.get(index++);
            return invoker.invoke(context, (CH) this);
        } else {
            return function.apply(context);
        }
    }
}
