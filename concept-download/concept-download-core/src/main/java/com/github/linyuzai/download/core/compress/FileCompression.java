package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.web.ContentType;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 文件压缩。
 */
@Getter
@RequiredArgsConstructor
public class FileCompression extends AbstractCompression {

    /**
     * 压缩文件
     */
    @NonNull
    protected final File file;

    /**
     * 获得一个 {@link FileInputStream}。
     *
     * @return {@link FileInputStream}
     */
    @Override
    public InputStream openInputStream() throws IOException {
        return Files.newInputStream(file.toPath());
    }

    /**
     * 如果设置了名称则使用设置的名称，否则设置为文件名称。
     *
     * @return 自定义的名称或文件名称
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
     * 如果设置了 Content-Type 则使用设置的 Content-Type，
     * 否则尝试获取并设置为文件的 Content-Type。
     *
     * @return 自定义的 Content-Type 或文件的 Content-Type
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
     * 获得文件的长度。
     *
     * @return 文件长度
     */
    @Override
    public Long getLength() {
        return file.length();
    }

    /**
     * 本地文件本身相当于缓存。
     *
     * @return true
     */
    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    /**
     * 文件是否存在。
     *
     * @return 如果文件存在则返回 true
     */
    @Override
    public boolean isCacheExisted() {
        return file.exists();
    }

    /**
     * 直接返回文件的父目录。
     *
     * @return 文件的父目录
     */
    @Override
    public String getCachePath() {
        return file.getParent();
    }

    /**
     * 文件压缩描述。
     *
     * @return FileCompression(文件路径)
     */
    @Override
    public String getDescription() {
        return "FileCompression(" + file.getAbsolutePath() + ")";
    }

    /**
     * 删除文件。
     */
    @Override
    public void deleteCache() {
        if (isCacheEnabled() && isCacheExisted()) {
            boolean delete = file.delete();
        }
    }
}
