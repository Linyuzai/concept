package com.github.linyuzai.download.core.compress;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 内存压缩 / Memory compression
 */
public class MemoryCompression extends AbstractCompression {

    protected byte[] bytes;

    protected InputStream bytesInputStream;

    public MemoryCompression(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * @return 字节输入流 / Input stream of bytes
     */
    @Override
    public InputStream getInputStream() {
        return open();
    }

    private InputStream open() {
        if (bytesInputStream == null) {
            bytesInputStream = new ByteArrayInputStream(bytes);
        }
        return bytesInputStream;
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
    public void release() {
        if (bytesInputStream != null) {
            try {
                bytesInputStream.close();
            } catch (Throwable ignore) {
            }
        }
        bytesInputStream = null;
    }
}
