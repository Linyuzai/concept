package com.github.linyuzai.download.core.concept;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

/**
 * 一个 {@link Downloadable} 可能包含多个部分。
 * <p>
 * A {@link Downloadable} may contain multiple parts.
 */
public interface Part {

    /**
     * 获得 {@link InputStream}。
     * <p>
     * Get {@link InputStream}.
     *
     * @return {@link InputStream}
     */
    InputStream getInputStream();

    /**
     * 获得名称。
     * <p>
     * Get name.
     *
     * @return 名称
     * <p>
     * Name
     */
    String getName();

    /**
     * 获得路径。
     * <p>
     * Get path.
     *
     * @return 路径
     * <p>
     * Path
     */
    default String getPath() {
        return getName();
    }

    /**
     * 获得 Content-Type。
     * <p>
     * Get Content-Type.
     *
     * @return Content-Type
     */
    String getContentType();

    /**
     * 获得编码。
     * <p>
     * Get charset.
     *
     * @return 编码
     * <p>
     * Charset
     */
    Charset getCharset();

    /**
     * 获得长度，即字节数。
     * <p>
     * Get the length,that is, the number of bytes.
     *
     * @return 长度
     * <p>
     * Length
     */
    Long getLength();

    /**
     * 获得子节点。
     * <p>
     * Get sub parts.
     *
     * @return 子节点
     * <p>
     * Sub parts
     */
    default Collection<Part> getChildren() {
        return Collections.emptyList();
    }

    /**
     * 释放资源。
     * <p>
     * Release resources.
     */
    default void release() {
    }
}
