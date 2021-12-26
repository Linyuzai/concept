package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.contenttype.ContentType;
import lombok.AllArgsConstructor;

import java.io.*;

/**
 * 文件压缩 / File compression
 */
@AllArgsConstructor
public class FileCompression extends AbstractCompression {

    protected File file;

    /**
     * @return 文件输入流 / File input stream
     * @throws IOException I/O exception
     */
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

    /**
     * 如果设置了 Content Type 则使用设置的 Content Type / If the content type is set, the content type is used
     * 否则设置为文件的 Content Type /  Otherwise, set to file content type
     *
     * @return Content Type
     */
    @Override
    public String getContentType() {
        String contentType = super.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            setContentType(ContentType.file(file));
        }
        return super.getContentType();
    }

    /**
     * @return 文件长度 / File length
     */
    @Override
    public Long getLength() {
        return file.length();
    }

    /**
     * @return true
     */
    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    /**
     * @return 文件是否存在 / Does the file exist
     */
    @Override
    public boolean isCacheExisted() {
        return file.exists();
    }

    /**
     * @return 文件的父目录 / The parent directory of the file
     */
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
