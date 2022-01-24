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
 * 文件压缩。
 * <p>
 * File compression.
 */
@AllArgsConstructor
public class FileCompression extends AbstractCompression {

    @NonNull
    protected final File file;

    /**
     * 获得一个 {@link FileInputStream}
     * <p>
     * Get a {@link FileInputStream}
     *
     * @return {@link FileInputStream}
     */
    @SneakyThrows
    @Override
    public InputStream openInputStream() {
        return new FileInputStream(file);
    }

    /**
     * 如果设置了名称则使用设置的名称，
     * 否则设置为文件名称。
     * <p>
     * If the name is set, the set name is used;
     * otherwise, it is set to the file name.
     *
     * @return 自定义的名称或文件名称
     * <p>
     * Custom name or file name
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
     * 如果设置了 Content-Type 则使用设置的 Content-Type，
     * 否则尝试获取并设置为文件的 Content-Type。
     * <p>
     * If the Content-Type is set, the set Content-Type is used;
     * otherwise, try to get and set it to the Content-Type of the file.
     *
     * @return 自定义的 Content-Type 或文件的 Content-Type
     * <p>
     * Custom Content-Type or file Content-Type
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
     * 获得文件的长度。
     * <p>
     * Get the length of the file.
     *
     * @return 文件长度
     * <p>
     * File length
     */
    @Override
    public Long getLength() {
        return file.length();
    }

    /**
     * 本地文件本身相当于缓存。
     * <p>
     * The local file itself is equivalent to a cache.
     *
     * @return true
     */
    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    /**
     * 文件是否存在。
     * <p>
     * Does the file exist
     *
     * @return 如果文件存在则返回 true
     * <p>
     * Return true if the file exists
     */
    @Override
    public boolean isCacheExisted() {
        return file.exists();
    }

    /**
     * 直接返回文件的父目录。
     * <p>
     * Returns the parent directory of the file directly.
     *
     * @return 文件的父目录
     * <p>
     * The parent directory of the file
     */
    @Override
    public String getCachePath() {
        return file.getParent();
    }

    /**
     * 文件压缩描述。
     * <p>
     * File compression description.
     *
     * @return FileCompression(文件路径)
     * <p>
     * FileCompression(file path)
     */
    @Override
    public String getDescription() {
        return "FileCompression(" + file.getAbsolutePath() + ")";
    }

    /**
     * 删除文件。
     * <p>
     * Delete file.
     */
    @Override
    public void deleteCache() {
        if (isCacheEnabled() && isCacheExisted()) {
            boolean delete = file.delete();
        }
    }
}
