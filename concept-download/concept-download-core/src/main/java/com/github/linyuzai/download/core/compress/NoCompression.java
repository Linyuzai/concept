package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 不压缩 / No compression
 * 对于不压缩的下载操作提供统一的压缩实现类 / Provide a unified compression implementation class for uncompressed download operations
 */
@AllArgsConstructor
public class NoCompression implements Compression {

    protected Source source;

    @Override
    public InputStream getInputStream() throws IOException {
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
}
