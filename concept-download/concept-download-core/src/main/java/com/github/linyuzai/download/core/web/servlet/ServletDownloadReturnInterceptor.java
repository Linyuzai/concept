package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import com.github.linyuzai.download.core.exception.ErrorHolder;
import reactor.core.publisher.Mono;

public class ServletDownloadReturnInterceptor implements DownloadReturnInterceptor {

    @Override
    public Object intercept(Mono<Void> mono) {
        ErrorHolder holder = new ErrorHolder();
        mono.subscribe(unused -> {
        }, holder::set);
        holder.throwIfError();
        return null;
    }
}
