package com.github.linyuzai.download.web.servlet.response;

import com.github.linyuzai.download.core.response.DownloadResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * HttpServletResponse实现 / implementations by HttpServletResponse
 */
@Getter
@AllArgsConstructor
public class ServletDownloadResponse implements DownloadResponse {

    private HttpServletResponse response;

    @Override
    public OutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    @Override
    public void setStatusCode(int statusCode) {
        response.setStatus(statusCode);
    }

    @Override
    public void setContentType(String contentType) {
        if (contentType != null) {
            response.setContentType(contentType);
        }
    }

    @Override
    public void setContentLength(Long contentLength) {
        if (contentLength != null) {
            response.setContentLengthLong(contentLength);
        }
    }

    @Override
    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        response.addHeader(name, value);
    }
}
