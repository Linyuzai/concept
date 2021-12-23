package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.*;

import java.io.*;
import java.nio.charset.Charset;

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
            return file.getName();
        } else {
            return name;
        }
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

    /**
     * 如果是文件夹则会递归遍历内部的文件或子文件写入 / If it is a folder, it will recursively traverse the internal file or sub file write
     * 如果是文件夹则不支持范围指定 / Range assignment is not supported if it is a folder
     *
     * @param os      写入数据的输出流 / Output stream to write
     * @param range   写入的范围 / Range of writing
     * @param writer  具体操作字节或字符的处理类 / Handler to handle bytes or chars
     * @param handler 可对每一部分进行单独写入操作 / Do write for each part {@link Part}
     * @throws IOException I/O exception
     */
    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        String name = getName();
        if (range == null) {
            write0(os, null, writer, handler, file, name, name, true);
        } else {
            if (file.isFile()) {
                write0(os, range, writer, handler, file, name, name, true);
            } else {
                throw new DownloadException("Range not support: " + file.getAbsolutePath());
            }
        }
    }

    /**
     * 使用文件输入流进行递归写入 / Recursive writing using file input stream
     * 如果是文件夹也会回调 / If it is a folder, it will also be recalled
     * 但是其中的输入流为null / But the input stream is null
     */
    protected void write0(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler,
                          File file, String path, String name, boolean keepStruct) throws IOException {
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                Part part = new Part() {

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return fis;
                    }

                    @Override
                    public String getName() {
                        return name;
                    }

                    @Override
                    public String getPath() {
                        return path;
                    }

                    @Override
                    public Charset getCharset() {
                        return FileSource.this.getCharset();
                    }

                    @Override
                    public void write() throws IOException {
                        writer.write(getInputStream(), os, range, getCharset(), file.length());
                    }
                };
                handler.handle(part);
            }
        } else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                if (keepStruct) {
                    Part part = new Part() {

                        @Override
                        public InputStream getInputStream() throws IOException {
                            return null;
                        }

                        @Override
                        public String getName() {
                            return name;
                        }

                        @Override
                        public String getPath() {
                            return path + File.separator;
                        }

                        @Override
                        public Charset getCharset() {
                            return FileSource.this.getCharset();
                        }

                        @Override
                        public void write() throws IOException {

                        }
                    };
                    handler.handle(part);
                }
            } else {
                for (File f : files) {
                    String newName = f.getName();
                    String newPath = keepStruct ? path + File.separator + newName : newName;
                    write0(os, range, writer, handler, f, newPath, newName, keepStruct);
                }
            }
        }
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
