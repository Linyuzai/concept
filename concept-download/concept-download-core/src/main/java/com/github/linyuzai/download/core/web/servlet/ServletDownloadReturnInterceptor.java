package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import reactor.core.publisher.Mono;

public class ServletDownloadReturnInterceptor implements DownloadReturnInterceptor {

    @Override
    public Object intercept(Mono<Void> mono) {
        mono.subscribe();
        return null;
    }
}
