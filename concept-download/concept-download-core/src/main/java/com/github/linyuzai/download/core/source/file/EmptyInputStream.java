package com.github.linyuzai.download.core.source.file;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class EmptyInputStream extends InputStream {

    private volatile boolean closed;

    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    @Override
    public int available() throws IOException {
        ensureOpen();
        return 0;
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        ensureOpen();
        return -1;
    }

    @Override
    public long skip(long n) throws IOException {
        ensureOpen();
        return 0L;
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        ensureOpen();
        if (n > 0) {
            throw new EOFException();
        }
    }

    @Override
    public void close() {
        closed = true;
    }
}
