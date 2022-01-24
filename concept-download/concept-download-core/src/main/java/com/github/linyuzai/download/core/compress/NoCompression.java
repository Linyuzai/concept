package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 不压缩。
 * <p>
 * No compression.
 */
@AllArgsConstructor
public class NoCompression implements Compression {

    protected Source source;

    /**
     * 直接返回 {@link Source#getInputStream()}。
     * <p>
     * Directly return {@link Source#getInputStream()}.
     *
     * @return {@link Source#getInputStream()}
     */
    @Override
    public InputStream getInputStream() {
        return source.getInputStream();
    }

    /**
     * 直接返回 {@link Source#getName()}。
     * <p>
     * Directly return {@link Source#getName()}.
     *
     * @return {@link Source#getName()}
     */
    @Override
    public String getName() {
        return source.getName();
    }

    /**
     * 直接返回 {@link Source#getContentType()}。
     * <p>
     * Directly return {@link Source#getContentType()}.
     *
     * @return {@link Source#getContentType()}
     */
    @Override
    public String getContentType() {
        return source.getContentType();
    }

    /**
     * 直接返回 {@link Source#getCharset()}。
     * <p>
     * Directly return {@link Source#getCharset()}.
     *
     * @return {@link Source#getCharset()}
     */
    @Override
    public Charset getCharset() {
        return source.getCharset();
    }

    /**
     * 直接返回 {@link Source#getLength()}。
     * <p>
     * Directly return {@link Source#getLength()}.
     *
     * @return {@link Source#getLength()}
     */
    @Override
    public Long getLength() {
        return source.getLength();
    }

    /**
     * 不压缩描述。
     * <p>
     * No compression description.
     *
     * @return NoCompression:{@link Source#getDescription()}
     */
    @Override
    public String getDescription() {
        return "NoCompression:" + source.getDescription();
    }

    /**
     * 直接返回 {@link Source#getParts()}。
     * <p>
     * Directly return {@link Source#getParts()}.
     *
     * @return {@link Source#getParts()}
     */
    @Override
    public Collection<Part> getParts() {
        return source.getParts();
    }

    /**
     * 直接调用 {@link Source#release()}
     */
    @Override
    public void release() {
        source.release();
    }
}
