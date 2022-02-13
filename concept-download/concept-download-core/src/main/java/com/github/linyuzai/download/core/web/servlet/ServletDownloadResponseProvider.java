package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.web.DownloadResponse;
import com.github.linyuzai.download.core.web.DownloadResponseProvider;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;

/**
 * {@link ServletDownloadResponse} 的提供者。
 */
public class ServletDownloadResponseProvider implements DownloadResponseProvider {

    @Override
    public Mono<DownloadResponse> getResponse(DownloadContext context) {
        Object resp = context.getOptions().getResponse();
        Object[] parameters = context.getOptions().getDownloadMethod().getParameters();
        HttpServletResponse response = getHttpServletResponse(resp, parameters);
        if (response == null) {
            throw new DownloadException("HttpServletResponse not found");
        } else {
            return Mono.just(new ServletDownloadResponse(response));
        }
    }

    /**
     * 如果下载参数中配置了响应对象则直接返回，
     * 判断方法参数中是否存在，有则返回该参数，
     * 否则使用 {@link ServletRequestAttributes} 获取。
     *
     * @param response   下载参数中的响应
     * @param parameters 方法入参
     * @return {@link HttpServletResponse} 或 null
     */
    protected HttpServletResponse getHttpServletResponse(Object response, Object[] parameters) {
        if (response instanceof HttpServletResponse) {
            return (HttpServletResponse) response;
        } else if (response instanceof ServletServerHttpResponse) {
            return ((ServletServerHttpResponse) response).getServletResponse();
        }
        for (Object parameter : parameters) {
            if (parameter instanceof HttpServletResponse) {
                return (HttpServletResponse) parameter;
            } else if (parameter instanceof ServletServerHttpResponse) {
                return ((ServletServerHttpResponse) parameter).getServletResponse();
            }
        }
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getResponse();
    }
}
