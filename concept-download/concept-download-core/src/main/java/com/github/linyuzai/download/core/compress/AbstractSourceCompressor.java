package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

public abstract class AbstractSourceCompressor implements SourceCompressor {

    @Override
    public Compressible compress(Source source, DownloadWriter writer, String cachePath, DownloadContext context) throws IOException {
        String cacheName = getCacheName(source, context);
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        if (cacheEnable) {
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File cache = new File(dir, cacheName);
            if (cache.exists()) {
                return new LocalFileCompressed(cache);
            }

            FileOutputStream fos = new FileOutputStream(cache);
            doCompress(source, fos, writer);
            return new LocalFileCompressed(cache);
        } else {
            InMemoryCompressed compressed = new InMemoryCompressed(source, writer, this);
            compressed.setName(cacheName);
            return compressed;
        }
    }

    public abstract void doCompress(Source source, OutputStream os, DownloadWriter writer) throws IOException;

    public String getCacheName(Source source, DownloadContext context) {
        String cacheName = context.getOptions().getCompressCacheName();
        String suffix = getSuffix();
        if (cacheName == null || cacheName.isEmpty()) {
            if (source.isSingle()) {
                String sourceName = source.getName();
                int index = sourceName.lastIndexOf(CompressFormat.DOT);
                if (index == -1) {
                    return sourceName + suffix;
                } else {
                    return sourceName.substring(0, index) + suffix;
                }
            } else {
                return getDefaultName(context) + suffix;
            }
        } else {
            if (cacheName.endsWith(suffix)) {
                return cacheName;
            } else {
                return cacheName + suffix;
            }
        }
    }

    public String getDefaultName(DownloadContext context) {
        Method method = context.getOptions().getDownloadMethod().getMethod();
        return method.getDeclaringClass().getSimpleName() + "_" + method.getName();
    }

    public abstract String getSuffix();
}
