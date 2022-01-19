package com.github.linyuzai.download.core.compress;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 内存压缩 / Memory compression
 */
@AllArgsConstructor
public class MemoryCompression extends AbstractCompression {

    @NonNull
    protected final byte[] bytes;

    /**
     * @return 字节输入流 / Input stream of bytes
     */
    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public Charset getCharset() {
        return null;
    }

    @Override
    public Long getLength() {
        return (long) bytes.length;
    }

    @Override
    public String getDescription() {
        return "MemoryCompression(" + getLength() + ")";
    }
}
