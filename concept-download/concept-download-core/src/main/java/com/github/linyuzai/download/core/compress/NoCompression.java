package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 不压缩。
 */
@Getter
@RequiredArgsConstructor
public class NoCompression implements Compression {

    /**
     * 单个的 {@link Source}
     */
    protected final Source source;

    /**
     * 直接返回 {@link Source#getInputStream()}。
     *
     * @return {@link Source#getInputStream()}
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return source.getInputStream();
    }

    /**
     * 直接返回 {@link Source#getName()}。
     *
     * @return {@link Source#getName()}
     */
    @Override
    public String getName() {
        return source.getName();
    }

    /**
     * 直接返回 {@link Source#getContentType()}。
     *
     * @return {@link Source#getContentType()}
     */
    @Override
    public String getContentType() {
        return source.getContentType();
    }

    /**
     * 直接返回 {@link Source#getCharset()}。
     *
     * @return {@link Source#getCharset()}
     */
    @Override
    public Charset getCharset() {
        return source.getCharset();
    }

    /**
     * 直接返回 {@link Source#getLength()}。
     *
     * @return {@link Source#getLength()}
     */
    @Override
    public Long getLength() {
        return source.getLength();
    }

    /**
     * 不压缩描述。
     *
     * @return NoCompression:{@link Source#getDescription()}
     */
    @Override
    public String getDescription() {
        return "NoCompression(" + source.getDescription() + ")";
    }

    /**
     * 直接返回 {@link Source#getParts()}。
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
