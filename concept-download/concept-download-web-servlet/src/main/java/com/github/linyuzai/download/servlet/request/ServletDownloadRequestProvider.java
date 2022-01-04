package com.github.linyuzai.download.servlet.request;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.request.DownloadRequest;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ServletDownloadRequest提供者 / Provider of ServletDownloadRequest
 */
public class ServletDownloadRequestProvider implements DownloadRequestProvider {

    @Override
    public DownloadRequest getRequest(DownloadContext context) {
        Object req = context.getOptions().getRequest();
        Object[] parameters = context.getOptions().getDownloadMethod().getParameters();
        HttpServletRequest request = getHttpServletRequest(req, parameters);
        if (request == null) {
            throw new DownloadException("HttpServletRequest not found");
        } else {
            return new ServletDownloadRequest(request);
        }
    }

    /**
     * 如果下载参数中配置了请求对象则直接返回 / If the request object is configured in the download parameters, it will be returned directly
     * 判断方法参数中是否存在，有则返回该参数 / Judge whether the method parameter exists. If so, return the parameter
     * 否则使用 {@link ServletRequestAttributes} 获取 / Otherwise, use {@link ServletRequestAttributes} to get
     *
     * @param request    下载参数中的请求 / Request in download options
     * @param parameters 方法入参 / Method parameters
     * @return {@link HttpServletRequest}
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
