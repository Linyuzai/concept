package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 不压缩 / No compression
 */
@AllArgsConstructor
public class NoCompression implements Compression {

    protected Source source;

    @Override
    public InputStream getInputStream() {
        return source.getInputStream();
    }

    @Override
    public String getName() {
        return source.getName();
    }

    @Override
    public String getContentType() {
        return source.getContentType();
    }

    @Override
    public Charset getCharset() {
        return source.getCharset();
    }

    @Override
    public Long getLength() {
        return source.getLength();
    }

    @Override
    public Collection<Part> getParts() {
        return source.getParts();
    }

    @Override
    public void release() {
        source.release();
    }
}
