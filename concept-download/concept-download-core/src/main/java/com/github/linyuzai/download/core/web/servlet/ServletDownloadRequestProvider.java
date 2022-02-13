package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.web.DownloadRequest;
import com.github.linyuzai.download.core.web.DownloadRequestProvider;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

/**
 * {@link ServletDownloadRequest} 的提供者。
 */
public class ServletDownloadRequestProvider implements DownloadRequestProvider {

    @Override
    public Mono<DownloadRequest> getRequest(DownloadContext context) {
        Object req = context.getOptions().getRequest();
        Object[] parameters = context.getOptions().getDownloadMethod().getParameters();
        HttpServletRequest request = getHttpServletRequest(req, parameters);
        if (request == null) {
            throw new DownloadException("HttpServletRequest not found");
        } else {
            return Mono.just(new ServletDownloadRequest(request));
        }
    }

    /**
     * 如果下载参数中配置了请求对象则直接返回，
     * 判断方法参数中是否存在，有则返回该参数，
     * 否则使用 {@link ServletRequestAttributes} 获取。
     *
     * @param request    下载参数中的请求
     * @param parameters 方法入参
     * @return {@link HttpServletRequest} 或 null
     */
    protected HttpServletRequest getHttpServletRequest(Object request, Object[] parameters) {
        if (request instanceof HttpServletRequest) {
            return (HttpServletRequest) request;
        } else if (request instanceof ServletServerHttpRequest) {
            return ((ServletServerHttpRequest) request).getServletRequest();
        }
        for (Object parameter : parameters) {
            if (parameter instanceof HttpServletRequest) {
                return (HttpServletRequest) parameter;
            } else if (parameter instanceof ServletServerHttpRequest) {
                return ((ServletServerHttpRequest) parameter).getServletRequest();
            }
        }
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }
}
