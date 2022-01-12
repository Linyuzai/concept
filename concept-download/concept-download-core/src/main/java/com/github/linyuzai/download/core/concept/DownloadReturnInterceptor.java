package com.github.linyuzai.download.core.concept;

import reactor.core.publisher.Mono;

public interface DownloadReturnInterceptor {

    Object intercept(Mono<Void> mono);
}
