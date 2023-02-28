package com.github.linyuzai.chain.reactor;

import com.github.linyuzai.chain.core.*;

import java.util.List;
import java.util.function.Function;

public class ReactorChainFactory<T, CTX extends Context, CH extends Chain<T, CTX>> implements ChainFactory<T, CTX, CH> {

    @SuppressWarnings("unchecked")
    @Override
    public CH create(int index, List<? extends Invoker<T, CTX, CH>> invokers, Function<CTX, Return<T>> function) {
        return (CH) new ReactorChain<>(this, index, invokers, function);
    }
}
