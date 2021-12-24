package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.file.FileSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.*;

/**
 * 文件压缩 / File compression
 */
@AllArgsConstructor
public class FileCompression extends AbstractCompression {

    protected File file;

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
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
     * 删除文件 / Delete file
     */
    @Override
    public void deleteCache() {
        if (isCacheEnabled() && isCacheExisted()) {
            boolean delete = file.delete();
        }
    }
}
