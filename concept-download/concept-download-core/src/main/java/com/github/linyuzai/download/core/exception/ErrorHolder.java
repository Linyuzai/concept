package com.github.linyuzai.download.core.exception;

import lombok.SneakyThrows;

public class ErrorHolder {

    private Throwable e;

    public void set(Throwable e) {
        this.e = e;
    }

    @SneakyThrows
    public void throwIfError() {
        if (e == null) {
            return;
        }
        throw e;
    }
}
