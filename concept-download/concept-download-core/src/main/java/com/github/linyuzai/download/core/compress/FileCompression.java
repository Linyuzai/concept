package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.file.FileSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件压缩 / File compression
 */
@AllArgsConstructor
public class FileCompression extends AbstractCompression {

    protected File file;

    protected String contentType;

    @Override
    public String getName() {
        String name = super.getName();
        if (name == null || name.isEmpty()) {
            setName(file.getName());
        }
        return super.getName();
    }

    @Override
    public String getContentType() {
        String contentType = super.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            setContentType(ContentType.file(file));
        }
        return super.getContentType();
    }

    @Override
    public Long getLength() {
        return file.length();
    }

    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    @Override
    public boolean isCacheExisted() {
        return file.exists();
    }

    @Override
    public String getCachePath() {
        return file.getParent();
    }

    /**
     * 通过文件下载源写数据 / Write data through file download source {@link FileSource}
     *
     * @param os      写入数据的输出流 / Output stream to write
     * @param range   写入的范围 / Range of writing
     * @param writer  具体操作字节或字符的处理类 / Handler to handle bytes or chars
     * @param handler 可对每一部分进行单独写入操作 / Do write for each part {@link Part}
     * @throws IOException I/O exception
     */
    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        FileSource source = new FileSource.Builder().file(file).build();
        source.write(os, range, writer, handler);
    }

    /**
     * 删除文件 / Delete file
     */
    @Override
    public void deleteCache() {
        if (isCacheEnabled() && isCacheExisted()) {
            boolean delete = file.delete();
        }
    }
}
