package com.github.linyuzai.chain.core;

public interface Invoker<T, CTX extends Context, CH extends Chain<T, CTX>> {

    Return<T> invoke(CTX context, CH chain);
}
