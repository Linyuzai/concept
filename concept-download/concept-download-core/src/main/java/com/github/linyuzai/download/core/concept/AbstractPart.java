package com.github.linyuzai.download.core.concept;

import lombok.AllArgsConstructor;

import java.nio.charset.Charset;

@AllArgsConstructor
public abstract class AbstractPart implements Downloadable.Part {

    private Downloadable downloadable;

    @Override
    public String getName() {
        return downloadable.getName();
    }

    @Override
    public String getPath() {
        return getName();
    }

    @Override
    public String getContentType() {
        return downloadable.getContentType();
    }

    @Override
    public Charset getCharset() {
        return downloadable.getCharset();
    }

    @Override
    public Long getLength() {
        return downloadable.getLength();
    }
}
