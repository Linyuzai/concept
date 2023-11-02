package com.github.linyuzai.download.core.web;

import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 下载响应。
 */
public interface DownloadResponse {

    /**
     * 写入。
     *
     * @return {@link Void} 的 {@link Mono}
     */
    Object write(Consumer<OutputStream> consumer, Supplier<Object> next, Runnable onComplete) throws IOException;

    /**
     * 设置状态码。
     *
     * @param statusCode 状态码
     */
    void setStatusCode(int statusCode);

    /**
     * 设置文件名。
     *
     * @param filename 文件名
     */

    default void setAttachment(String filename) {
        setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encode(filename));
    }

    /**
     * 设置 inline。
     */
    default void setInline(String filename) {
        setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encode(filename));
    }

    /**
     * 进行 url 编码。
     *
     * @param s 需要编码的字符串
     * @return 编码后的字符串
     */
    @SneakyThrows
    default String encode(String s) {
        return URLEncoder.encode(String.valueOf(s), "UTF-8");
    }

    /**
     * 设置 Content-Type。
     *
     * @param contentType Content-Type
     */
    void setContentType(String contentType);

    /**
     * 设置 Content-Length。
     *
     * @param contentLength Content-Length
     */
    void setContentLength(Long contentLength);

    /**
     * 设置 'Accept-Ranges:bytes' 请求头。
     */
    default void setBytesAcceptRanges() {
        setHeader("Accept-Ranges", "bytes");
    }

    /**
     * 设置 'Content-Range'。
     *
     * @param start 开始位置
     * @param end   结束位置
     * @param total 总大小
     */
    default void setContentRange(long start, long end, long total) {
        setHeader("Content-Range", "bytes " + start + "-" + end + "/" + total);
    }

    /**
     * 设置响应头。
     *
     * @param name  响应头的名称
     * @param value 响应头的值
     */
    void setHeader(String name, String value);

    /**
     * 批量设置响应头。
     *
     * @param headers 响应头 {@link Map}
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
     * 添加响应头.
     *
     * @param name  响应头的名称
     * @param value 响应头的值
     */
    void addHeader(String name, String value);

    /**
     * 批量添加响应头.
     *
     * @param headers 响应头 {@link Map}
     */
    default void addHeaders(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 刷新缓冲区。
     */
    default void flush() {

    }
}
