package com.github.linyuzai.download.core.exception;

import lombok.SneakyThrows;

/**
 * 异常持有者。
 */
public class ErrorHolder {

    /**
     * 异常
     */
    private Throwable e;

    /**
     * 设置异常。
     *
     * @param e 异常
     */
    public void set(Throwable e) {
        this.e = e;
    }

    /**
     * 如果存在异常则抛出。
     */
    @SneakyThrows
    public void throwIfError() {
        if (e == null) {
            return;
        }
        throw e;
    }
}
