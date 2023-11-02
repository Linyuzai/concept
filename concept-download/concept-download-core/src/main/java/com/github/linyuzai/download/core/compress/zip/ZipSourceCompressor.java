package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressor;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.ContentType;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 使用 {@link ZipOutputStream} 进行压缩。
 */
public class ZipSourceCompressor extends AbstractSourceCompressor<ZipOutputStream> {

    /**
     * 新建一个 {@link ZipOutputStream}。
     *
     * @param os      被包装的输出流
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return {@link ZipOutputStream}
     */
    @Override
    public ZipOutputStream newOutputStream(OutputStream os, Source source, DownloadContext context) {
        return new ZipOutputStream(os);
    }

    /**
     * 写之前添加一个 {@link ZipEntry}。
     *
     * @param part {@link Part}
     * @param os   {@link ZipOutputStream}
     */
    @SneakyThrows
    @Override
    public void beforeWrite(Part part, ZipOutputStream os) {
        os.putNextEntry(new ZipEntry(part.getPath()));
    }

    /**
     * 写入之后关闭 {@link ZipEntry}。
     *
     * @param part {@link Part}
     * @param os   {@link ZipOutputStream}
     */
    @SneakyThrows
    @Override
    public void afterWrite(Part part, ZipOutputStream os) {
        os.closeEntry();
    }

    /**
     * 获得 zip 文件扩展后缀。
     *
     * @return .zip
     */
    @Override
    public String getSuffix() {
        return CompressFormat.ZIP_SUFFIX;
    }

    /**
     * 获得 zip 文件的 Content-Type。
     *
     * @return application/x-zip-compressed
     */
    @Override
    public String getContentType() {
        return ContentType.Application.X_ZIP_COMPRESSED;
    }

    /**
     * 获得文件的压缩格式。
     *
     * @return zip
     */
    @Override
    public String getFormat() {
        return CompressFormat.ZIP;
    }
}
