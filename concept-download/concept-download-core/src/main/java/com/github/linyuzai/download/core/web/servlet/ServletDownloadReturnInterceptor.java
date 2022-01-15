package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import com.github.linyuzai.download.core.exception.DownloadException;
import lombok.Setter;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class ServletDownloadReturnInterceptor implements DownloadReturnInterceptor {

    @Override
    public Object intercept(Mono<Void> mono) {
        ErrorHolder holder = new ErrorHolder();
        mono.subscribe(unused -> {
        }, holder::set);
        holder.throwIfError();
        return null;
    }

    static class ErrorHolder {

        Throwable e;

        void set(Throwable e) {
            this.e = e;
        }

        @SneakyThrows
        void throwIfError() {
            if (e == null) {
                return;
            }
            throw e;
        }
    }
}
