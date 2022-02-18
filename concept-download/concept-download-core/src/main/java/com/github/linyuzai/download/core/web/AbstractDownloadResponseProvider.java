package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.options.DownloadMethod;
import reactor.core.publisher.Mono;

/**
 * {@link DownloadResponseProvider} 抽象类。
 */
public abstract class AbstractDownloadResponseProvider implements DownloadResponseProvider {

    /**
     * 获得 {@link DownloadResponse} 对应的 {@link Mono}。
     * 如果指定了 {@link DownloadResponse} 则直接返回。
     *
     * @param context {@link DownloadContext}
     * @return {@link DownloadResponse} 对应的 {@link Mono}
     */
    @Override
    public Mono<DownloadResponse> getResponse(DownloadContext context) {
        Object response = context.getOptions().getResponse();
        if (response instanceof DownloadResponse) {
            return Mono.just((DownloadResponse) response);
        }
        DownloadMethod method = context.getOptions().getDownloadMethod();
        Object[] parameters = method == null ? new Object[]{} : method.getParameters();
        return doGetResponse(response, parameters, context);
    }

    /**
     * 获得 {@link DownloadResponse} 对应的 {@link Mono}。
     *
     * @param response   指定响应
     * @param parameters 方法参数
     * @param context    {@link DownloadContext}
     * @return {@link DownloadResponse} 对应的 {@link Mono}
     */
    public abstract Mono<DownloadResponse> doGetResponse(Object response, Object[] parameters, DownloadContext context);
}
