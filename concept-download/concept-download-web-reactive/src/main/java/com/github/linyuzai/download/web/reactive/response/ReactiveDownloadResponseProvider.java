package com.github.linyuzai.download.web.reactive.response;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.response.DownloadResponse;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * ServletDownloadResponse提供者 / Provider of ServletDownloadResponse
 */
@AllArgsConstructor
public class ReactiveDownloadResponseProvider implements DownloadResponseProvider {

    private DataBufferFactory factory;

    public ReactiveDownloadResponseProvider() {
        this(new DefaultDataBufferFactory());
    }

    @Override
    public DownloadResponse getResponse(DownloadContext context) {
        Object resp = context.getOptions().getResponse();
        Object[] parameters = context.getOptions().getDownloadMethod().getParameters();
        ServerHttpResponse response = getServerHttpResponse(resp, parameters);
        if (response == null) {
            throw new DownloadException("ServerHttpResponse not found");
        } else {
            return new ReactiveDownloadResponse(response, factory);
        }
    }

    /**
     * 如果下载参数中配置了响应对象则直接返回 / If the response object is configured in the download parameters, it will be returned directly
     * 判断方法参数中是否存在，有则返回该参数 / Judge whether the method parameter exists. If so, return the parameter
     *
     * @param response   下载参数中的响应 / Response in download options
     * @param parameters 方法入参 / Method parameters
     * @return {@link ServerHttpResponse}
     */
    protected ServerHttpResponse getServerHttpResponse(Object response, Object[] parameters) {
        if (response instanceof ServerHttpResponse) {
            return (ServerHttpResponse) response;
        }
        for (Object parameter : parameters) {
            if (parameter instanceof ServerHttpResponse) {
                return (ServerHttpResponse) parameter;
            }
        }
        return null;
    }
}
