package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 不压缩 / No compression
 * 对于不压缩的下载操作提供统一的压缩实现类 / Provide a unified compression implementation class for uncompressed download operations
 */
@AllArgsConstructor
public class NoCompression implements Compression {

    protected Source source;

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
    public void write(OutputStream os, Range range, DownloadWriter writer) throws IOException {
        source.write(os, range, writer);
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        source.write(os, range, writer, handler);
    }
}
