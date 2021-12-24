package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 可下载的对象 / An object can download
 *
 * @see Source
 * @see Compression
 */
public interface Downloadable extends Cacheable {

    /**
     * @return 名称 / name
     */
    String getName();

    /**
     * @return Content Type
     */
    String getContentType();

    /**
     * @return 编码 / charset
     */
    Charset getCharset();

    /**
     * @return 字节数 / bytes count
     */
    Long getLength();

    /**
     * 写入操作 / write
     *
     * @param os     写入数据的输出流 / Output stream to write
     * @param range  写入的范围 / Range of writing
     * @param writer 写入执行器 / Executor of writing
     * @throws IOException I/O exception
     */
    default void write(OutputStream os, Range range, DownloadWriter writer) throws IOException {
        write(os, range, writer, new WriteHandler() {
            @Override
            public void handle(Part part) throws IOException {
                WriteHandler.super.handle(part);
            }
        });
    }

    /**
     * 写入操作 / Write
     *
     * @param os      写入数据的输出流 / Output stream to write
     * @param range   写入的范围 / Range of writing
     * @param writer  具体操作字节或字符的处理类 / Handler to handle bytes or chars
     * @param handler 可对每一部分进行单独写入操作 / Do write for each part {@link Part}
     * @throws IOException I/O exception
     */
    void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException;

    /**
     * 如果有多个部分，会单独回调每个部分 / Callback each part if multiple
     * 比如，一个文件夹里面的多个文件 / For example, multiple files in a folder
     */
    interface WriteHandler {

        /**
         * 每个部分都会回调 / Callback each part
         *
         * @throws IOException I/O exception
         */
        default void handle(Part part) throws IOException {
            part.write();
        }
    }

    /**
     * 一个下载对象可能有多个部分 / A download object may have multiple parts
     */
    interface Part {

        /**
         * @return 输入流 / Input stream
         * @throws IOException I/O exception
         */
        InputStream getInputStream() throws IOException;

        /**
         * @return 名称 / name
         */
        String getName();

        /**
         * @return 一个相对的路径，参考压缩文件内部的文件路径 / A relative path, referring to the file path inside the compressed file
         */
        String getPath();

        /**
         * @return Content Type
         */
        String getContentType();

        /**
         * @return 编码 / charset
         */
        Charset getCharset();

        /**
         * @return 字节数 / bytes count
         */
        Long getLength();

        /**
         * 执行写 / Execute write
         *
         * @throws IOException I/O exception
         */
        void write() throws IOException;
    }
}
