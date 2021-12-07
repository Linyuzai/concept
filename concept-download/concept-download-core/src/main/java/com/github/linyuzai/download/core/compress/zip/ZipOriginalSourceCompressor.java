package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.compress.OriginalSourceCompressor;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
public class ZipOriginalSourceCompressor implements OriginalSourceCompressor {

    @Override
    public boolean support(String format, DownloadContext context) {
        return CompressFormat.ZIP.equals(format);
    }

    @Override
    public CompressedSource compress(OriginalSource source, SourceWriter writer, String cachePath, DownloadContext context) throws IOException {
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        String cacheName = context.getOptions().getCompressCacheName();
        String finalCacheName;
        if (cacheName == null || cacheName.isEmpty()) {
            if (source.isSingle()) {
                finalCacheName = source.getName();
            } else {
                finalCacheName = CompressedSource.NAME + CompressFormat.ZIP_SUFFIX;
            }
        } else {
            if (cacheName.endsWith(CompressFormat.ZIP_SUFFIX)) {
                finalCacheName = cacheName;
            } else {
                finalCacheName = cacheName + CompressFormat.ZIP_SUFFIX;
            }
        }
        if (cacheEnable) {
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File file = new File(dir, finalCacheName);
            if (file.exists()) {
                return new ZipFileCompressedSource(file);
            }
            Charset charset = source.getCharset();
            try (FileOutputStream fos = new FileOutputStream(file);
                 ZipOutputStream zos = charset == null ?
                         new ZipOutputStream(fos) : new ZipOutputStream(fos, charset)) {
                source.write(zos, null, writer, new Source.WriteHandler() {
                    @Override
                    public void handle(Source.Part part) throws IOException {
                        zos.putNextEntry(new ZipEntry(part.getPath()));
                        Source.WriteHandler.super.handle(part);
                        zos.closeEntry();
                    }
                });
            }
            return new ZipFileCompressedSource(file);
        } else {
            return new ZipMemoryCompressedSource(source, finalCacheName);
        }
    }
}
