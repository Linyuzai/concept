package com.github.linyuzai.chain.reactor;

import com.github.linyuzai.chain.core.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class ReactorChain<T, CTX extends Context, CH extends Chain<T, CTX>> implements Chain<T, CTX> {

    private final ChainFactory<T, CTX, CH> factory;

    private final int index;

    private final List<? extends Invoker<T, CTX, CH>> invokers;

    private final Function<CTX, Return<T>> function;

    @SuppressWarnings("unchecked")
    @Override
    public Return<T> next(CTX context) {
        if (index < invokers.size()) {
            Invoker<T, CTX, CH> invoker = invokers.get(index);
            Chain<T, CTX> chain = factory.create(index + 1, invokers, function);
            return invoker.invoke(context, (CH) chain);
        } else {
            return function.apply(context);
        }
    }
}
