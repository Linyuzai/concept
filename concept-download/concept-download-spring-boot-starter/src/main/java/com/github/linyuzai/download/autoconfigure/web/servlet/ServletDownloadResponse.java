package com.github.linyuzai.download.autoconfigure.web.servlet;

import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 持有 {@link HttpServletResponse} 的 {@link DownloadResponse}，用于 webmvc。
 */
@Getter
@RequiredArgsConstructor
public class ServletDownloadResponse implements DownloadResponse {

    private final HttpServletResponse response;

    @Override
    public Object write(Consumer<OutputStream> consumer, Supplier<Object> next, Runnable onComplete) throws IOException {
        consumer.accept(response.getOutputStream());
        onComplete.run();
        return next.get();
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
