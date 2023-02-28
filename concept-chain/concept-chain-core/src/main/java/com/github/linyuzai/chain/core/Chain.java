package com.github.linyuzai.chain.core;

public interface Chain<T, CTX extends Context> {

    Return<T> next(CTX context);
}
