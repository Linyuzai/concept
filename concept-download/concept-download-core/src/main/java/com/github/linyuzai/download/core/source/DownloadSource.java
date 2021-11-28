package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * 下载源
 */
public interface DownloadSource {

    String getName();

    Charset getCharset();

    long getLength();

    void load() throws IOException;

    boolean isAsyncLoad();

    default void write(OutputStream os, Range range, SourceWriter writer) throws IOException {
        write(os, range, writer, new WriteHandler() {
            @Override
            public void handle(Target target) throws IOException {
                WriteHandler.super.handle(target);
            }
        });
    }

    /**
     * 遍历下载源中所有的数据
     *
     * @param reader 回调所有的数据流
     * @throws IOException
     */
    void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException;

    default Collection<DownloadSource> flatten() {
        return flatten(downloadSource -> true);
    }

    default Collection<DownloadSource> flatten(Predicate<DownloadSource> predicate) {
        if (predicate.test(this)) {
            return Collections.singletonList(this);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 读取下载源数据的读取器
     */
    interface WriteHandler {

        /**
         * 每次读到都会回调
         *
         * @param readable 数据路径
         * @throws IOException
         */
        default void handle(Target target) throws IOException {
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
