package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.ContentType;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 持有 {@link File} 的 {@link Source}。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileSource extends AbstractSource {

    /**
     * 持有的 {@link File}
     */
    @NonNull
    @Setter
    protected File file;

    /**
     * 持有的 {@link Part}
     */
    protected Part part;

    /**
     * 如果 {@link File#isFile()} 则返回 {@link FileInputStream}，
     * 否则返回 {@link EmptyInputStream}。
     *
     * @return {@link FileInputStream} 或 {@link EmptyInputStream}
     */
    @SneakyThrows
    @Override
    public InputStream openInputStream() {
        return file.isFile() ? new FileInputStream(file) : new EmptyInputStream();
    }

    /**
     * 如果没有指定名称则返回 {@link File#getName()}。
     *
     * @return 指定的名称或 {@link File#getName()}
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
     * 如果没有指定 Content-Type 则尝试使用 {@link ContentType#file(File)} 获取。
     *
     * @return 指定的 Content-Type 或 {@link ContentType#file(File)} 获得的或 null
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
     * 如果是文件则返回文件大小，如果是文件夹则返回整个文件夹的大小。
     *
     * @return 文件或整个文件夹大小
     */
    @Override
    public Long getLength() {
        return length0(file);
    }

    private long length0(File file) {
        if (file.isFile()) {
            return file.length();
        } else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return 0;
            }
            long length = 0;
            for (File f : files) {
                length += length0(f);
            }
            return length;
        }
    }

    /**
     * 如果是文件则返回 true，如果是文件夹则返回 false。
     *
     * @return 如果是文件则返回 true，如果是文件夹则返回 false
     */
    @Override
    public boolean isSingle() {
        return file.isFile();
    }

    /**
     * 直接返回 true。
     *
     * @return true
     */
    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    /**
     * 通过 {@link File#exists()} 获得。
     *
     * @return 如果文件存在则返回 true
     */
    @Override
    public boolean isCacheExisted() {
        return file.exists();
    }

    /**
     * 通过 {@link File#getParent()} 获得。
     *
     * @return 缓存目录
     */
    @Override
    public String getCachePath() {
        return file.getParent();
    }

    @Override
    public String getDescription() {
        return "FileSource(" + file.getAbsolutePath() + ")";
    }

    /**
     * 实时返回文件目录的结构。
     *
     * @return 一个文件或整个目录结构
     */
    @Override
    public Collection<Part> getParts() {
        if (part != null) {
            part.release();
        }
        String name = getName();
        part = new FilePart(file, name, name);
        List<Part> parts = new ArrayList<>();
        Resource.addPart(part, parts);
        return parts;
    }

    /**
     * 释放资源。
     */
    @Override
    public void release() {
        super.release();
        if (part != null) {
            part.release();
            part = null;
        }
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends FileSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        private File file;

        public B file(File file) {
            this.file = file;
            return (B) this;
        }

        @Override
        protected T build(T target) {
            target.setFile(file);
            return super.build(target);
        }

        @Override
        public T build() {
            return build((T) new FileSource());
        }
    }
}
