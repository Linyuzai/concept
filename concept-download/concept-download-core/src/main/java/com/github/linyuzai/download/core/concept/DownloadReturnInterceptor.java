package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.web.reactive.ReactiveDownloadReturnInterceptor;
import com.github.linyuzai.download.core.web.servlet.ServletDownloadReturnInterceptor;
import reactor.core.publisher.Mono;

/**
 * 下载返回拦截器。
 * 主要用于兼容 webmvc 和 webflux 的返回值处理
 *
 * @see ServletDownloadReturnInterceptor
 * @see ReactiveDownloadReturnInterceptor
 */
public interface DownloadReturnInterceptor {

    /**
     * 拦截下载返回值。
     *
     * @param mono 下载返回值
     * @return 最终返回值
     */
    Object intercept(Mono<Void> mono);
}
