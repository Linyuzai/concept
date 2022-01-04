package com.github.linyuzai.download.servlet.response;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.response.DownloadResponse;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * ServletDownloadResponse提供者 / Provider of ServletDownloadResponse
 */
public class ServletDownloadResponseProvider implements DownloadResponseProvider {

    @Override
    public DownloadResponse getResponse(DownloadContext context) {
        Object resp = context.getOptions().getResponse();
        Object[] parameters = context.getOptions().getDownloadMethod().getParameters();
        HttpServletResponse response = getHttpServletResponse(resp, parameters);
        if (response == null) {
            throw new DownloadException("HttpServletResponse not found");
        } else {
            return new ServletDownloadResponse(response);
        }
    }

    /**
     * 如果下载参数中配置了响应对象则直接返回 / If the response object is configured in the download parameters, it will be returned directly
     * 判断方法参数中是否存在，有则返回该参数 / Judge whether the method parameter exists. If so, return the parameter
     * 否则使用 {@link ServletRequestAttributes} 获取 / Otherwise, use {@link ServletRequestAttributes} to get
     *
     * @param response   下载参数中的响应 / Response in download options
     * @param parameters 方法入参 / Method parameters
     * @return {@link HttpServletResponse}
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
