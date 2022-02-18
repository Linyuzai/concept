package com.github.linyuzai.download.core.web.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.web.AbstractDownloadResponseProvider;
import com.github.linyuzai.download.core.web.DownloadResponse;
import com.github.linyuzai.download.core.web.DownloadResponseProvider;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * {@link ReactiveDownloadResponse} 的提供者。
 */
public class ReactiveDownloadResponseProvider extends AbstractDownloadResponseProvider {

    /**
     * 获得 {@link ReactiveDownloadResponse} 对应的 {@link Mono}。
     *
     * @param response   指定响应
     * @param parameters 方法参数
     * @param context    {@link DownloadContext}
     * @return {@link ReactiveDownloadResponse} 对应的 {@link Mono}
     */
    @Override
    public Mono<DownloadResponse> doGetResponse(Object response, Object[] parameters, DownloadContext context) {
        return getServerHttpResponse(response, parameters).map(it -> {
            if (it == null) {
                throw new DownloadException("ServerHttpResponse not found");
            } else {
                return new ReactiveDownloadResponse(it);
            }
        });
    }

    /**
     * 如果下载参数中配置了响应对象则直接返回，
     * 判断方法参数中是否存在，有则返回该参数，
     * 否则使用 {@link ReactiveDownloadHolder} 获取。
     *
     * @param response   下载参数中的响应
     * @param parameters 方法入参
     * @return {@link ServerHttpResponse} 对应的 {@link Mono}
     */
    protected Mono<ServerHttpResponse> getServerHttpResponse(Object response, Object[] parameters) {
        if (response instanceof ServerHttpResponse) {
            return Mono.just((ServerHttpResponse) response);
        }
        for (Object parameter : parameters) {
            if (parameter instanceof ServerHttpResponse) {
                return Mono.just((ServerHttpResponse) parameter);
            }
        }
        return ReactiveDownloadHolder.getResponse();
    }
}
