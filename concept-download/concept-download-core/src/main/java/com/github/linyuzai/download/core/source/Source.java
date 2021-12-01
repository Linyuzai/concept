package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface Source {

    String getName();

    Charset getCharset();

    long getLength();

    default void write(OutputStream os, Range range, SourceWriter writer) throws IOException {
        write(os, range, writer, new Source.WriteHandler() {
            @Override
            public void handle(Source.Target target) throws IOException {
                Source.WriteHandler.super.handle(target);
            }
        });
    }

    /**
     * 遍历下载源中所有的数据
     *
     * @throws IOException
     */
    void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException;

    /**
     * 读取下载源数据的读取器
     */
    interface WriteHandler {

        /**
         * 每次读到都会回调
         *
         * @throws IOException
         */
        default void handle(OriginalSource.Target target) throws IOException {
            target.write();
        }
    }

    interface Target {

        InputStream getInputStream() throws IOException;

        String getName();

        String getPath();

        Charset getCharset();

        void write() throws IOException;

    }
}
