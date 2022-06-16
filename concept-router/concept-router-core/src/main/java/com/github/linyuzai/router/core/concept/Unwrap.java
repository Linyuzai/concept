package com.github.linyuzai.router.core.concept;

@Deprecated
public interface Unwrap {

    @SuppressWarnings("unchecked")
    default <T> T unwrap() {
        return (T) this;
    }
}
