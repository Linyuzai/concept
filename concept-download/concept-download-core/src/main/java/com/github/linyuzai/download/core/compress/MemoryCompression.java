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
 * 内存压缩 / Memory compression
 */
@AllArgsConstructor
public class MemoryCompression extends AbstractCompression {

    private final Source source;

    @Override
    public InputStream getInputStream() throws IOException {
        return source.getInputStream();
    }

    @Override
    public String getName() {
        String name = super.getName();
        if (name == null || name.isEmpty()) {
            return source.getName();
        } else {
            return name;
        }
    }

    @Override
    public Charset getCharset() {
        return null;
    }

    @Override
    public Long getLength() {
        return null;
    }

    @Override
    public Collection<Part> getParts() {
        return source.getParts();
    }
}
