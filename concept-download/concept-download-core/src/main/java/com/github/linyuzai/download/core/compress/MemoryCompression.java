package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 内存压缩 / Memory compression
 */
@AllArgsConstructor
public class MemoryCompression extends AbstractCompression {

    private final Source source;

    /**
     * @return Source的输入流 / Input stream of source
     * @throws IOException I/O exception
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return source.getInputStream();
    }

    /**
     * 如果设置了名称则使用设置的名称 / If the name is set, the set name is used
     * 否则设置为文件名称 / Otherwise, set to file name
     *
     * @return 名称 / Name
     */
    @Override
    public String getName() {
        String name = super.getName();
        if (name == null || name.isEmpty()) {
            setName(source.getName());
        }
        return super.getName();
    }

    @Override
    public Charset getCharset() {
        return null;
    }

    @Override
    public Long getLength() {
        return null;
    }

    /**
     * @return Source 的 Parts / Parts of source
     */
    @Override
    public Collection<Part> getParts() {
        return source.getParts();
    }
}
