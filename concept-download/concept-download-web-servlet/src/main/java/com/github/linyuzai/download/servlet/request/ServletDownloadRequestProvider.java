package com.github.linyuzai.download.servlet.request;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.request.DownloadRequest;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ServletDownloadRequestProvider implements DownloadRequestProvider {

    @Override
    public DownloadRequest getRequest(DownloadContext context) {
        Object req = context.getOptions().getRequest();
        Object[] parameters =  context.getOptions().getDownloadMethod().getParameters();
        HttpServletRequest request = getHttpServletRequest(req, parameters);
        if (request == null) {
            throw new NullPointerException("HttpServletRequest not found");
        } else {
            return new ServletDownloadRequest(request);
        }
    }

    protected HttpServletRequest getHttpServletRequest(Object request, Object[] parameters) {
        if (request instanceof HttpServletRequest) {
            return (HttpServletRequest) request;
        }
        for (Object parameter : parameters) {
            if (parameter instanceof HttpServletRequest) {
                return (HttpServletRequest) parameter;
            }
        }
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }
}
