package com.github.linyuzai.download.autoconfigure.web.mock;

import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class MockDownloadResponse implements DownloadResponse {

    private final OutputStream os;

    @Override
    public Object write(Consumer<OutputStream> consumer, Supplier<Object> next, Runnable onComplete) throws IOException {
        consumer.accept(os);
        onComplete.run();
        return next.get();
    }

    @Override
    public void setStatusCode(int statusCode) {
    }

    @Override
    public void setContentType(String contentType) {
    }

    @Override
    public void setContentLength(Long contentLength) {
    }

    @Override
    public void setHeader(String name, String value) {
    }

    @Override
    public void addHeader(String name, String value) {
    }
}
