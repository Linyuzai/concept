package com.github.linyuzai.download.core.web.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.web.DownloadResponse;
import com.github.linyuzai.download.core.web.DownloadResponseProvider;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * ReactiveDownloadResponse提供者 / Provider of ReactiveDownloadResponse
 */
public class ReactiveDownloadResponseProvider implements DownloadResponseProvider {

    @Override
    public Mono<DownloadResponse> getResponse(DownloadContext context) {
        Object response = context.getOptions().getResponse();
        Object[] parameters = context.getOptions().getDownloadMethod().getParameters();
        return getServerHttpResponse(response, parameters).map(it -> {
            if (it == null) {
                throw new DownloadException("ServerHttpResponse not found");
            } else {
                return new ReactiveDownloadResponse(it);
            }
        });
    }

    /**
     * 如果下载参数中配置了响应对象则直接返回 / If the response object is configured in the download parameters, it will be returned directly
     * 判断方法参数中是否存在，有则返回该参数 / Judge whether the method parameter exists. If so, return the parameter
     *
     * @param response   下载参数中的响应 / Response in download options
     * @param parameters 方法入参 / Method parameters
     * @return {@link ServerHttpResponse}
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
