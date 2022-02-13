package com.github.linyuzai.download.core.concept;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

/**
 * 一个 {@link Resource} 可能包含多个部分。
 */
public interface Part {

    /**
     * 获得 {@link InputStream}。
     *
     * @return {@link InputStream}
     */
    InputStream getInputStream();

    /**
     * 获得名称。
     *
     * @return 名称
     */
    String getName();

    /**
     * 获得路径。
     *
     * @return 路径
     */
    default String getPath() {
        return getName();
    }

    /**
     * 获得 Content-Type。
     *
     * @return Content-Type
     */
    String getContentType();

    /**
     * 获得编码。
     *
     * @return 编码
     */
    Charset getCharset();

    /**
     * 获得长度，即字节数。
     *
     * @return 长度
     */
    Long getLength();

    /**
     * 获得子节点。
     *
     * @return 子节点
     */
    default Collection<Part> getChildren() {
        return Collections.emptyList();
    }

    /**
     * 释放资源。
     */
    default void release() {
    }
}
