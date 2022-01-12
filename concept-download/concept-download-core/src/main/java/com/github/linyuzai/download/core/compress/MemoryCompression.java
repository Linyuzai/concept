package com.github.linyuzai.download.core.compress;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 内存压缩 / Memory compression
 */
@AllArgsConstructor
public class MemoryCompression extends AbstractCompression {

    private final byte[] bytes;

    /**
     * @return 字节输入流 / Input stream of bytes
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public Charset getCharset() {
        return null;
    }

    @Override
    public Long getLength() {
        return null;
    }
}
