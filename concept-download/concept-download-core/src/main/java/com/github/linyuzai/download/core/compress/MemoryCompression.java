package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 内存压缩 / Memory compression
 */
@AllArgsConstructor
public class MemoryCompression extends AbstractCompression {

    private final Source source;

    private final DownloadWriter writer;

    private final AbstractSourceCompressor compressor;

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

    /**
     * 内存压缩直接操作响应的输出流 / Memory compression direct operation output stream of response
     * 压缩将会延迟到写入时触发 / Compression is delayed until writing response
     *
     * @param os      写入数据的输出流 / Output stream to write
     * @param range   写入的范围 / Range of writing
     * @param writer  具体操作字节或字符的处理类 / Handler to handle bytes or chars
     * @param handler 可对每一部分进行单独写入操作 / Do write for each part {@link Part}
     * @throws IOException I/O exception
     */
    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        compressor.doCompress(source, os, this.writer);
    }
}
