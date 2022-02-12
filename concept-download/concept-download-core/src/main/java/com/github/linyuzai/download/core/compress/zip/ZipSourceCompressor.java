package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressor;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.ContentType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 使用 {@link ZipOutputStream} 进行压缩。
 * <p>
 * Use {@link ZipOutputStream} for compression.
 */
@AllArgsConstructor
public class ZipSourceCompressor extends AbstractSourceCompressor<ZipOutputStream> {

    /**
     * 新建一个 {@link ZipOutputStream}
     * <p>
     * New a {@link ZipOutputStream}
     *
     * @param os      被包装的输出流
     *                <p>
     *                Wrapped output stream
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return
     */
    @Override
    public ZipOutputStream newOutputStream(OutputStream os, Source source, DownloadContext context) {
        return new ZipOutputStream(os);
    }

    /**
     * 写之前添加一个 {@link ZipEntry}。
     * <p>
     * Add a {@link ZipEntry} before writing.
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
     * <p>
     * Close {@link ZipEntry} after writing.
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
     * <p>
     * Get the zip file extension suffix.
     *
     * @return .zip
     */
    @Override
    public String getSuffix() {
        return CompressFormat.ZIP_SUFFIX;
    }

    /**
     * 获得 zip 文件的 Content-Type。
     * <p>
     * Get the Content-Type of the zip file.
     *
     * @return application/x-zip-compressed
     */
    @Override
    public String getContentType() {
        return ContentType.Application.X_ZIP_COMPRESSED;
    }

    @Override
    public String getFormat() {
        return CompressFormat.ZIP;
    }
}
