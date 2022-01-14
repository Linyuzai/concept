package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import com.github.linyuzai.download.core.exception.DownloadException;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class ServletDownloadReturnInterceptor implements DownloadReturnInterceptor {

    @Override
    public Object intercept(Mono<Void> mono) {
        mono.subscribe(unused -> {
        }, e -> {
            if (e instanceof DownloadException) {
                throw (DownloadException) e;
            } else {
                throw new DownloadException(e);
            }
        });
        return null;
    }
}
