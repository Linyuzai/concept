package com.github.linyuzai.download.core.web;

import lombok.SneakyThrows;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 响应，如http / Response, such as http
 */
public interface DownloadResponse {

    default Mono<DownloadResponse> write(Consumer<OutputStream> consumer) {
        consumer.accept(getOutputStream());
        return Mono.just(this);
    }

    /**
     * 获得输出流 / Get output stream
     *
     * @return 输出流 / Output stream
     */
    OutputStream getOutputStream();

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

    default void setAttachment(String filename) {
        setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encode(filename));
    }

    /**
     * 设置inline / Set inline
     */
    default void setInline(String filename) {
        setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encode(filename));
    }

    @SneakyThrows
    default String encode(String s) {
        return URLEncoder.encode(String.valueOf(s), "UTF-8");
    }

    /**
     * 设置ContentType / Set ContentType
     *
     * @param contentType ContentType
     */
    void setContentType(String contentType);

    void setContentLength(Long contentLength);

    default void setBytesAcceptRanges() {
        setHeader("Accept-Ranges", "bytes");
    }

    default void setContentRange(long start, long end, long total) {
        setHeader("Content-Range", "bytes " + start + "-" + end + "/" + total);
    }

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

    default void flush() {

    }
}
