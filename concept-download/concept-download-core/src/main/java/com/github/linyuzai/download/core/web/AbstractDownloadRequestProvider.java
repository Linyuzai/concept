package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.options.DownloadMethod;
import reactor.core.publisher.Mono;

/**
 * {@link DownloadRequestProvider} 抽象类。
 */
public abstract class AbstractDownloadRequestProvider implements DownloadRequestProvider {

    /**
     * 获得 {@link DownloadRequest} 对应的 {@link Mono}。
     * 如果指定了 {@link DownloadRequest} 则直接返回。
     *
     * @param context {@link DownloadContext}
     * @return {@link DownloadRequest} 对应的 {@link Mono}
     */
    @Override
    public Mono<DownloadRequest> getRequest(DownloadContext context) {
        Object request = context.getOptions().getRequest();
        if (request instanceof DownloadRequest) {
            return Mono.just((DownloadRequest) request);
        }
        DownloadMethod method = context.getOptions().getDownloadMethod();
        Object[] parameters = method == null ? new Object[]{} : method.getParameters();
        return doGetRequest(request, parameters, context);
    }

    /**
     * 获得 {@link DownloadRequest} 对应的 {@link Mono}。
     *
     * @param request    指定请求
     * @param parameters 方法参数
     * @param context    {@link DownloadContext}
     * @return {@link DownloadRequest} 对应的 {@link Mono}
     */
    public abstract Mono<DownloadRequest> doGetRequest(Object request, Object[] parameters, DownloadContext context);
}
