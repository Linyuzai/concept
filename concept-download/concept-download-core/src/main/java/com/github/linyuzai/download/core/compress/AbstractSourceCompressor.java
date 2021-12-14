package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * 进行了统一的缓存处理 / Unified cache processing
 */
public abstract class AbstractSourceCompressor implements SourceCompressor {

    /**
     * 如果没有启用缓存，使用内存压缩 / Use memory compression if caching is not enabled
     * 内存压缩会将压缩操作延后到写入响应时触发 / Memory compression delays the compression operation when writing response
     * 如果启用缓存并且缓存存在，直接使用缓存 / Use the cache directly if caching is enabled and the cache exists
     * 如果启用缓存并且缓存不存在，压缩到本地缓存文件 / Compress to the local cache file if caching is enabled and the cache does not exist
     *
     * @param source    {@link Source}
     * @param writer    {@link DownloadWriter}
     * @param cachePath 缓存路径 / The path of cache
     * @param context   Context of download
     * @return
     * @throws IOException
     */
    @Override
    public Compressible compress(Source source, DownloadWriter writer, String cachePath, DownloadContext context) throws IOException {
        Compressible result;
        String cacheName = getCacheName(source, context);
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        if (cacheEnable) {
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File cache = new File(dir, cacheName);
            if (cache.exists()) {
                result = new LocalFileCompressed(cache);
            } else {
                FileOutputStream fos = new FileOutputStream(cache);
                doCompress(source, fos, writer);
                result = new LocalFileCompressed(cache);
            }

        } else {
            InMemoryCompressed compressed = new InMemoryCompressed(source, writer, this);
            compressed.setName(cacheName);
            result = compressed;
        }
        return result;
    }

    /**
     * 执行压缩 / Perform compression
     *
     * @param source 被压缩的对象 / Object to compress
     * @param os     写入的输出流 / Output stream to write
     * @param writer 写入执行器 / Executor of writing
     * @throws IOException I/O exception
     */
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
