package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.Compressible;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
public class ZipSourceCompressor implements SourceCompressor {

    @Override
    public boolean support(String format, DownloadContext context) {
        return CompressFormat.ZIP.equals(format);
    }

    @Override
    public Compressible compress(Source source, DownloadWriter writer, String cachePath, DownloadContext context) throws IOException {
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        String cacheName = context.getOptions().getCompressCacheName();
        String finalCacheName;
        if (cacheName == null || cacheName.isEmpty()) {
            if (source.isSingle()) {
                finalCacheName = source.getName();
            } else {
                finalCacheName = getDefaultName(context) + CompressFormat.ZIP_SUFFIX;
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
                return new ZipFileCompressible(file);
            }
            Charset charset = source.getCharset();
            try (FileOutputStream fos = new FileOutputStream(file);
                 ZipOutputStream zos = charset == null ?
                         new ZipOutputStream(fos) : new ZipOutputStream(fos, charset)) {
                source.write(zos, null, writer, new Downloadable.WriteHandler() {
                    @Override
                    public void handle(Downloadable.Part part) throws IOException {
                        zos.putNextEntry(new ZipEntry(part.getPath()));
                        Downloadable.WriteHandler.super.handle(part);
                        zos.closeEntry();
                    }
                });
            }
            return new ZipFileCompressible(file);
        } else {
            return new ZipMemoryCompressible(source, finalCacheName);
        }
    }

    private String getDefaultName(DownloadContext context) {
        Method method = context.getOptions().getDownloadMethod().getMethod();
        return method.getDeclaringClass().getSimpleName() + "_" + method.getName();
    }
}
