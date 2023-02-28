package com.github.linyuzai.chain.core;

public interface ReturnFactory {

    <T> Return<T> create();
}
