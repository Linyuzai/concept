package com.github.linyuzai.download.servlet.request;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.request.DownloadRequest;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ServletDownloadRequestProvider implements DownloadRequestProvider, DownloadContextInitializer {

    @Override
    public DownloadRequest getRequest(DownloadContext context) {
        Object req = context.getOptions().getRequest();
        Object[] args = (Object[]) context.getOptions().getArgs();
        HttpServletRequest request = getHttpServletRequest(req, args);
        if (request == null) {
            throw new NullPointerException("HttpServletRequest not found");
        } else {
            return new ServletDownloadRequest(request);
        }
    }

    protected HttpServletRequest getHttpServletRequest(Object request, Object[] args) {
        if (request instanceof HttpServletRequest) {
            return (HttpServletRequest) request;
        }
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }

    @Override
    public void initialize(DownloadContext context) {
        DownloadRequest request = getRequest(context);
        context.set(DownloadRequest.class, request);
    }
}
