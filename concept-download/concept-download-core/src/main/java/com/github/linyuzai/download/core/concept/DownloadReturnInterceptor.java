package com.github.linyuzai.download.core.concept;

import reactor.core.publisher.Mono;

/**
 * 下载返回拦截器。
 * <p>
 * Download return interceptor.
 */
public interface DownloadReturnInterceptor {

    /**
     * 拦截下载返回值。
     * <p>
     * Intercept download return value.
     *
     * @param mono 下载返回值
     *             <p>
     *             Download return value
     * @return 最终返回值
     * <p>
     * Final return value
     */
    Object intercept(Mono<Void> mono);
}
