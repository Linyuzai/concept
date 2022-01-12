package com.github.linyuzai.download.core.web.reactive;

import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import reactor.core.publisher.Mono;

public class ReactiveDownloadReturnInterceptor implements DownloadReturnInterceptor {

    @Override
    public Object intercept(Mono<Void> mono) {
        return mono;
    }
}
