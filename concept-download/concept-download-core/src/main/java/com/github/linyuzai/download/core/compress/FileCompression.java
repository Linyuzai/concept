package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.web.ContentType;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 文件压缩 / File compression
 */
@AllArgsConstructor
public class FileCompression extends AbstractCompression {

    @NonNull
    protected final File file;

    /**
     * @return 文件输入流 / File input stream
     */
    @SneakyThrows
    @Override
    public InputStream openInputStream() {
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
        if (!StringUtils.hasText(name)) {
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
        if (!StringUtils.hasText(contentType)) {
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

    @Override
    public String getDescription() {
        return "FileCompression(" + file.getAbsolutePath() + ")";
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
