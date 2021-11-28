package com.github.linyuzai.download.core.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface DownloadResponse {

    OutputStream getOutputStream() throws IOException;

    void setStatusCode(int statusCode);

    void setFilename(String filename);

    void setContentType(String contentType);

    void setHeader(String name, String value);

    default void setHeaders(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            setHeader(entry.getKey(), entry.getValue());
        }
    }

    void addHeader(String name, String value);

    default void addHeaders(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }
}
