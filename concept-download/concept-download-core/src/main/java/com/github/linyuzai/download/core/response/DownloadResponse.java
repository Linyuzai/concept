package com.github.linyuzai.download.core.response;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 响应，如http / Response, such as http
 */
public interface DownloadResponse {

    /**
     * 获得输出流 / Get output stream
     *
     * @return 输出流 / Output stream
     * @throws IOException I/O exception
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * 设置状态码 / Set status code
     *
     * @param statusCode 状态码 / Status code
     */
    void setStatusCode(int statusCode);

    /**
     * 设置文件名 / Set file name
     *
     * @param filename 文件名 / File name
     */
    @SneakyThrows
    default void setFilename(String filename) {
        String encodeFilename = URLEncoder.encode(String.valueOf(filename), "UTF-8");
        setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodeFilename);
    }

    /**
     * 设置inline / Set inline
     */
    default void setInline() {
        setHeader("Content-Disposition", "inline");
    }

    /**
     * 设置ContentType / Set ContentType
     *
     * @param contentType ContentType
     */
    void setContentType(String contentType);

    /**
     * 设置响应头 / Set response header
     *
     * @param name  响应头的名称 / Name of response header
     * @param value 响应头的值 / Value of response header
     */
    void setHeader(String name, String value);

    /**
     * 批量设置响应头 / Set response headers
     *
     * @param headers 响应头Map / Map of response headers
     */
    default void setHeaders(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            setHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加响应头 / Add response header
     *
     * @param name  响应头的名称 / Name of response header
     * @param value 响应头的值 / Value of response header
     */
    void addHeader(String name, String value);

    /**
     * 批量添加响应头 / Add response headers
     *
     * @param headers 响应头Map / Map of response headers
     */
    default void addHeaders(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }
}
