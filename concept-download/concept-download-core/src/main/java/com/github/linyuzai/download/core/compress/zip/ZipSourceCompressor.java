package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressor;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.web.ContentType;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 使用ZIP压缩的压缩器 / Compressor using zip compression
 */
@AllArgsConstructor
public class ZipSourceCompressor extends AbstractSourceCompressor {

    /**
     * 支持ZIP格式 / Support ZIP format
     *
     * @param format  压缩格式 / Compression format
     * @param context 下载上下文 / Context of download
     * @return 是否支持该压缩格式 / If support this compressed format
     */
    @Override
    public boolean support(String format, DownloadContext context) {
        return CompressFormat.ZIP.equals(format);
    }

    /**
     * 新建一个ZipOutputStream / new a ZipOutputStream
     * {@link ZipOutputStream#ZipOutputStream(OutputStream, Charset)}
     */
    @Override
    public OutputStream newOutputStream(OutputStream os, Source source) {
        return new ZipOutputStream(os);
    }

    @SneakyThrows
    @Override
    public void beforeWrite(Part part, OutputStream os) {
        ((ZipOutputStream) os).putNextEntry(new ZipEntry(part.getPath()));
    }

    @SneakyThrows
    @Override
    public void afterWrite(Part part, OutputStream os) {
        ((ZipOutputStream) os).closeEntry();
    }

    /**
     * @return .zip后缀 / Use suffix .zip
     */
    @Override
    public String getSuffix() {
        return CompressFormat.ZIP_SUFFIX;
    }

    /**
     * @return application/x-zip-compressed
     */
    @Override
    public String getContentType() {
        return ContentType.Application.X_ZIP_COMPRESSED;
    }
}
