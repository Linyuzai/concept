package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.source.Source;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

/**
 * 可下载的对象 / An object can download
 *
 * @see Source
 * @see Compression
 */
public interface Downloadable extends Part, Cacheable {

    /**
     * @return 名称 / name
     */
    String getName();

    /**
     * @return Content Type
     */
    String getContentType();

    /**
     * @return 编码 / charset
     */
    Charset getCharset();

    /**
     * @return 字节数 / bytes count
     */
    Long getLength();

    default Collection<Part> getParts() {
        return Collections.singletonList(this);
    }
}
