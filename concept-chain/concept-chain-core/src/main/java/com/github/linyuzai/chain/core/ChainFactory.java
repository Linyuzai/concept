package com.github.linyuzai.chain.core;

import java.util.List;
import java.util.function.Function;

public interface ChainFactory<T, CTX extends Context, CH extends Chain<T, CTX>> {

    CH create(int index, List<? extends Invoker<T, CTX, CH>> invokers, Function<CTX, Return<T>> function);
}
