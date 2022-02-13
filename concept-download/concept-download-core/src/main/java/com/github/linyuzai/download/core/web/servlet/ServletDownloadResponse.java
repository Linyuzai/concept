package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 持有 {@link HttpServletResponse} 的 {@link DownloadResponse}，用于 webmvc。
 */
@Getter
@AllArgsConstructor
public class ServletDownloadResponse implements DownloadResponse {

    private HttpServletResponse response;

    @SneakyThrows
    @Override
    public OutputStream getOutputStream() {
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
