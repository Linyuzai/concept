package com.github.linyuzai.download.servlet.response;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.response.DownloadResponse;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

public class ServletDownloadResponseProvider implements DownloadResponseProvider {

    @Override
    public DownloadResponse getResponse(DownloadContext context) {
        Object resp = context.getOptions().getResponse();
        Object[] parameters = context.getOptions().getDownloadMethod().getParameters();
        HttpServletResponse response = getHttpServletResponse(resp, parameters);
        if (response == null) {
            throw new NullPointerException("HttpServletResponse not found");
        } else {
            return new ServletDownloadResponse(response);
        }
    }

    protected HttpServletResponse getHttpServletResponse(Object response, Object[] parameters) {
        if (response instanceof HttpServletResponse) {
            return (HttpServletResponse) response;
        }
        for (Object parameter : parameters) {
            if (parameter instanceof HttpServletResponse) {
                return (HttpServletResponse) parameter;
            }
        }
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getResponse();
    }
}
