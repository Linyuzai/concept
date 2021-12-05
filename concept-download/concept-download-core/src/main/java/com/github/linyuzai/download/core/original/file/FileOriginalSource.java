package com.github.linyuzai.download.core.original.file;

import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.original.AbstractOriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.NonNull;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 文件下载源
 * 该下载源持有一个文件对象，可能是文件，可能是目录
 */
public class FileOriginalSource extends AbstractOriginalSource {

    /**
     * 持有的文件
     */
    private final File file;

    private FileOriginalSource(@NonNull File file, Charset charset, boolean asyncLoad) {
        this.file = file;
        setCharset(charset);
        setAsyncLoad(asyncLoad);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public long getLength() {
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

    @Override
    public void load() throws IOException {

    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        if (range == null) {
            write0(os, null, writer, handler, file, getName(), true);
        } else {
            if (file.isFile()) {
                write0(os, range, writer, handler, file, getName(), true);
            } else {
                throw new DownloadException("Range not support: " + file.getAbsolutePath());
            }
        }
    }

    protected void write0(OutputStream os, Range range, SourceWriter writer, WriteHandler handler, File file, String path, boolean keepStruct) throws IOException {
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                Part part = new Part() {

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return fis;
                    }

                    @Override
                    public String getName() {
                        return FileOriginalSource.this.getName();
                    }

                    @Override
                    public String getPath() {
                        return path;
                    }

                    @Override
                    public Charset getCharset() {
                        return FileOriginalSource.this.getCharset();
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
                            return FileOriginalSource.this.getName();
                        }

                        @Override
                        public String getPath() {
                            return path + File.separator;
                        }

                        @Override
                        public Charset getCharset() {
                            return FileOriginalSource.this.getCharset();
                        }

                        @Override
                        public void write() throws IOException {

                        }
                    };
                    handler.handle(part);
                }
            } else {
                for (File f : files) {
                    String newPath = keepStruct ? path + File.separator + f.getName() : f.getName();
                    write0(os, range, writer, handler, f, newPath, keepStruct);
                }
            }
        }
    }

    public static class Builder {

        private File file;

        private Charset charset;

        private boolean asyncLoad;

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder asyncLoad(boolean asyncLoad) {
            this.asyncLoad = asyncLoad;
            return this;
        }

        public FileOriginalSource build() {
            return new FileOriginalSource(file, charset, asyncLoad);
        }
    }
}
