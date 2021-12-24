package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.source.AbstractSource;
import lombok.*;

import java.io.*;
import java.util.Collection;
import java.util.Collections;

/**
 * 文件下载源 / A source that holds a file
 * 该下载源持有一个文件对象，可能是文件，可能是目录 / The source holds a file object, which may be a file or a directory
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FileSource extends AbstractSource {

    @NonNull
    protected File file;

    @Override
    public InputStream getInputStream() throws IOException {
        return file.isFile() ? new FileInputStream(file) : null;
    }

    /**
     * 如果没有指定名称 / If no name is specified
     * 返回文件名称 / Return file name
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

    /**
     * 如果是文件则返回文件大小 / If it is a file, the file size is returned
     * 如果是文件夹则返回整个文件夹的大小 / If it is a folder, returns the size of the entire folder
     *
     * @return 文件或文件夹大小 / File or folder size
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
     * 如果是文件则返回true / Returns true if it is a file
     * 如果是文件夹则返回false / False if it is a folder
     *
     * @return 是否是单个的 / If single
     */
    @Override
    public boolean isSingle() {
        return file.isFile();
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

    @Override
    public Collection<Part> getParts() {
        String name = getName();
        return Collections.singletonList(new FilePart(file, name, name));
    }

    @Override
    public String toString() {
        return "FileSource{" +
                "file=" + file +
                '}';
    }

    public static class Builder extends AbstractSource.Builder<FileSource, Builder> {

        private File file;

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public FileSource build() {
            return super.build(new FileSource(file));
        }
    }
}
