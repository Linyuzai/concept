package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.DownloadWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface Downloadable extends Cacheable {

    String getName();

    Charset getCharset();

    long getLength();

    default void write(OutputStream os, Range range, DownloadWriter writer) throws IOException {
        write(os, range, writer, new WriteHandler() {
            @Override
            public void handle(Part part) throws IOException {
                WriteHandler.super.handle(part);
            }
        });
    }

    /**
     * 遍历下载源中所有的数据
     *
     * @throws IOException
     */
    void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException;

    /**
     * 读取下载源数据的读取器
     */
    interface WriteHandler {

        /**
         * 每次读到都会回调
         *
         * @throws IOException
         */
        default void handle(Part part) throws IOException {
            part.write();
        }
    }

    interface Part {

        InputStream getInputStream() throws IOException;

        String getName();

        String getPath();

        Charset getCharset();

        void write() throws IOException;

    }
}
